# Milestone 3 — Moteur d'Autorisation Tridimensionnel

## Contexte

Mohsinon n'est pas une simple application CRUD avec un RBAC plat. La plateforme confie une **gouvernance locale autonome** à chaque mosquée : l'Imam et le Comité administrent *leur* espace sans devenir des administrateurs globaux. Ce Milestone construit le moteur d'autorisation central qui sera réutilisé par tous les modules futurs (Mosques, Donations, Volunteers, Initiatives, Projects, Reputation).

> [!IMPORTANT]
> Ce Milestone ne crée PAS les modules métier (Mosques, Donations, etc.). Il construit uniquement le **moteur d'autorisation** et ses fondations. Les modules consommeront ce moteur plus tard.

---

## 1. Modèle de Domaine

L'autorisation s'évalue selon trois dimensions indépendantes :

```
Dimension 1: IDENTITÉ GLOBALE          Dimension 2: APPARTENANCE CONTEXTUELLE       Dimension 3: CAPACITÉ MÉTIER
┌──────────────────────┐              ┌──────────────────────────────────┐          ┌──────────────────────────────┐
│  User (UUID)         │              │  Membership (Entity)             │          │  PermissionType (Enum)       │
│  └── GlobalRole(s)   │              │  ├── userId                      │          │  ├── MOSQUE_VIEW             │
│      ├── USER        │              │  ├── resourceType ("MOSQUE",etc) │          │  ├── MOSQUE_UPDATE           │
│      ├── VOLUNTEER   │              │  ├── resourceId (UUID)           │          │  ├── MOSQUE_MANAGE_MEMBERS   │
│      ├── DONOR       │              │  ├── membershipRole (Enum)       │          │  ├── MOSQUE_VERIFY           │
│      └── ADMIN       │              │  │   ├── IMAM                    │          │  ├── DONATION_CREATE         │
│                      │              │  │   ├── COMMITTEE_PRESIDENT     │          │  ├── DONATION_MANAGE         │
└──────────────────────┘              │  │   ├── COMMITTEE_MEMBER        │          │  ├── DONATION_VERIFY         │
                                      │  │   ├── TREASURER               │          │  ├── INITIATIVE_CREATE       │
                                      │  │   ├── DONATION_MANAGER        │          │  ├── INITIATIVE_APPROVE      │
                                      │  │   ├── VOLUNTEER_COORDINATOR   │          │  ├── PROJECT_MANAGE          │
                                      │  │   └── LOCAL_MODERATOR         │          │  ├── VOLUNTEER_MANAGE        │
                                      │  ├── status (ACTIVE, REVOKED...) │          │  ├── IMPACT_VERIFY           │
                                      │  └── expiresAt (nullable)        │          │  └── ADMIN_ALL               │
                                      └──────────────────────────────────┘          └──────────────────────────────┘
```

### Principe fondamental : **Deny by Default**

Aucune permission n'est implicite. Toute action nécessite une évaluation explicite. Le flux est :

1. L'utilisateur est-il `ACTIVE` ? Sinon → **REFUSÉ**.
2. L'utilisateur possède-t-il `ROLE_ADMIN` ? Si oui → **AUTORISÉ** (avec audit).
3. La permission est-elle accordée globalement (via `GlobalRole → PermissionType` mapping) ? Si oui → **AUTORISÉ**.
4. Un contexte (ressource) est-il fourni ? Si non → **REFUSÉ**.
5. Existe-t-il un `Membership` `ACTIVE` et non expiré pour cet utilisateur sur cette ressource, dont le `MembershipRole` accorde cette permission ? Si oui → **AUTORISÉ**.
6. Sinon → **REFUSÉ**.

### Entités de domaine

| Entité / Value Object | Package | Rôle |
|---|---|---|
| `GlobalRole` (Entity) | `core.security.authorization` | Rôle global assigné à un utilisateur |
| `GlobalRoleType` (Enum) | `core.security.authorization` | `USER`, `VOLUNTEER`, `DONOR`, `ADMIN` |
| `PermissionType` (Enum) | `core.security.authorization` | Catalogue type-safe de toutes les capacités métier |
| `Membership` (Entity) | `core.security.authorization` | Appartenance contextuelle (user + resource + role + status + expiration) |
| `MembershipRole` (Enum) | `core.security.authorization` | `IMAM`, `COMMITTEE_PRESIDENT`, `COMMITTEE_MEMBER`, `TREASURER`, `DONATION_MANAGER`, `VOLUNTEER_COORDINATOR`, `LOCAL_MODERATOR` |
| `MembershipStatus` (Enum) | `core.security.authorization` | `ACTIVE`, `PENDING_APPROVAL`, `REVOKED`, `EXPIRED` |
| `ResourceContext` (Value Object) | `core.security.authorization` | Encapsulation `(resourceType, resourceId)` |

### Mapping Permissions — Rôles Globaux (In-Memory Static)

Le mapping `GlobalRoleType → Set<PermissionType>` est défini en code (pas en base), car il représente les règles métier fondamentales de la plateforme :

```java
USER      → { MOSQUE_VIEW, DONATION_CREATE, INITIATIVE_CREATE }
VOLUNTEER → { MOSQUE_VIEW, DONATION_CREATE, INITIATIVE_CREATE, VOLUNTEER_MANAGE }
DONOR     → { MOSQUE_VIEW, DONATION_CREATE, DONATION_MANAGE, INITIATIVE_CREATE }
ADMIN     → { ADMIN_ALL } // Bypass total
```

### Mapping Permissions — Rôles de Membership (In-Memory Static)

```java
IMAM                  → { MOSQUE_VIEW, MOSQUE_UPDATE, MOSQUE_MANAGE_MEMBERS, MOSQUE_VERIFY,
                           DONATION_MANAGE, DONATION_VERIFY, INITIATIVE_APPROVE,
                           PROJECT_MANAGE, VOLUNTEER_MANAGE, IMPACT_VERIFY }
COMMITTEE_PRESIDENT   → { MOSQUE_VIEW, MOSQUE_UPDATE, MOSQUE_MANAGE_MEMBERS,
                           DONATION_MANAGE, DONATION_VERIFY, INITIATIVE_APPROVE,
                           PROJECT_MANAGE, VOLUNTEER_MANAGE }
COMMITTEE_MEMBER      → { MOSQUE_VIEW, MOSQUE_UPDATE, DONATION_MANAGE, VOLUNTEER_MANAGE }
TREASURER             → { MOSQUE_VIEW, DONATION_MANAGE, DONATION_VERIFY }
DONATION_MANAGER      → { MOSQUE_VIEW, DONATION_MANAGE }
VOLUNTEER_COORDINATOR → { MOSQUE_VIEW, VOLUNTEER_MANAGE }
LOCAL_MODERATOR       → { MOSQUE_VIEW, MOSQUE_UPDATE, INITIATIVE_APPROVE }
```

> [!NOTE]
> Ces mappings sont des configurations Java statiques immuables dans une classe `PermissionRegistry`. Aucune jointure SQL n'est nécessaire pour la résolution des permissions. Seul le lookup de membership (1 requête) est en base.

---

## 2. Modèle Relationnel (Migration Flyway V2)

```sql
-- V2__init_authorization_schema.sql

-- 1. Table: global_roles (Rôles globaux de la plateforme)
CREATE TABLE IF NOT EXISTS global_roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_global_roles_name UNIQUE (name)
);

-- 2. Table: user_global_roles (Attribution N:N User <-> GlobalRole)
CREATE TABLE IF NOT EXISTS user_global_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES global_roles(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    assigned_by UUID REFERENCES users(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX IF NOT EXISTS idx_user_global_roles_user ON user_global_roles(user_id);

-- 3. Table: memberships (Appartenances contextuelles)
CREATE TABLE IF NOT EXISTS memberships (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    resource_type VARCHAR(50) NOT NULL,
    resource_id UUID NOT NULL,
    membership_role VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    expires_at TIMESTAMP WITH TIME ZONE,
    assigned_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_membership_unique UNIQUE (user_id, resource_type, resource_id, membership_role)
);

CREATE INDEX IF NOT EXISTS idx_memberships_user ON memberships(user_id);
CREATE INDEX IF NOT EXISTS idx_memberships_resource ON memberships(resource_type, resource_id);
CREATE INDEX IF NOT EXISTS idx_memberships_active ON memberships(user_id, status);

-- 4. Seed: Initial global roles
INSERT INTO global_roles (id, name, description, created_at, updated_at, version) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'ROLE_USER', 'Standard platform user', NOW(), NOW(), 0),
    ('a0000000-0000-0000-0000-000000000002', 'ROLE_VOLUNTEER', 'Registered volunteer', NOW(), NOW(), 0),
    ('a0000000-0000-0000-0000-000000000003', 'ROLE_DONOR', 'Active donor', NOW(), NOW(), 0),
    ('a0000000-0000-0000-0000-000000000004', 'ROLE_ADMIN', 'Platform administrator', NOW(), NOW(), 0);
```

> [!IMPORTANT]
> **Pas de table `permissions`** en base. Les permissions sont un enum Java (`PermissionType`) et leur mapping vers les rôles est défini statiquement dans `PermissionRegistry`. Cela évite des jointures coûteuses à chaque vérification d'autorisation et garantit la cohérence applicative — les permissions ne peuvent pas être « accidentellement modifiées » par un script SQL.

---

## 3. Principales Classes & Interfaces

### 3.1 Enums (Type-Safe Capabilities)

#### `GlobalRoleType.java`
```java
public enum GlobalRoleType {
    ROLE_USER, ROLE_VOLUNTEER, ROLE_DONOR, ROLE_ADMIN
}
```

#### `MembershipRole.java`
```java
public enum MembershipRole {
    IMAM, COMMITTEE_PRESIDENT, COMMITTEE_MEMBER, TREASURER,
    DONATION_MANAGER, VOLUNTEER_COORDINATOR, LOCAL_MODERATOR
}
```

#### `MembershipStatus.java`
```java
public enum MembershipStatus {
    ACTIVE, PENDING_APPROVAL, REVOKED, EXPIRED
}
```

#### `PermissionType.java`
```java
public enum PermissionType {
    MOSQUE_VIEW, MOSQUE_UPDATE, MOSQUE_MANAGE_MEMBERS, MOSQUE_VERIFY,
    DONATION_CREATE, DONATION_MANAGE, DONATION_VERIFY,
    INITIATIVE_CREATE, INITIATIVE_APPROVE,
    PROJECT_MANAGE,
    VOLUNTEER_MANAGE,
    IMPACT_VERIFY,
    ADMIN_ALL
}
```

### 3.2 Entités JPA

#### `GlobalRole.java`
Entité JPA mappée à `global_roles`, hérite de `BaseEntity`. Contient `name` (unique) et `description`.

#### `Membership.java`
Entité JPA mappée à `memberships`, hérite de `BaseEntity`. Contient :
- `userId` (UUID)
- `resourceType` (String — ex: `"MOSQUE"`)
- `resourceId` (UUID — ID de la mosquée spécifique)
- `membershipRole` (MembershipRole enum)
- `status` (MembershipStatus enum)
- `expiresAt` (Instant nullable — pour permissions temporaires)
- `assignedBy` (UUID nullable — traçabilité de qui a accordé l'accès)

Méthodes de domaine : `isEffective()` (status=ACTIVE && !expired), `revoke()`, `isExpired()`.

#### `ResourceContext.java` (Value Object)
```java
public record ResourceContext(String resourceType, UUID resourceId) {
    public static ResourceContext mosque(UUID mosqueId) {
        return new ResourceContext("MOSQUE", mosqueId);
    }
    public static ResourceContext project(UUID projectId) {
        return new ResourceContext("PROJECT", projectId);
    }
}
```

### 3.3 Repositories

#### `GlobalRoleRepository.java`
```java
public interface GlobalRoleRepository extends JpaRepository<GlobalRole, UUID> {
    Optional<GlobalRole> findByName(String name);
}
```

#### `UserGlobalRoleRepository.java`  
Custom repository ou via requêtes JPQL sur la table de jointure `user_global_roles` :
```java
Set<GlobalRoleType> findRolesByUserId(UUID userId);
boolean hasRole(UUID userId, GlobalRoleType role);
```

#### `MembershipRepository.java`
```java
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    List<Membership> findByUserIdAndStatus(UUID userId, MembershipStatus status);
    List<Membership> findByUserIdAndResourceTypeAndResourceIdAndStatus(
        UUID userId, String resourceType, UUID resourceId, MembershipStatus status);
    List<Membership> findByResourceTypeAndResourceIdAndStatus(
        String resourceType, UUID resourceId, MembershipStatus status);
    boolean existsByUserIdAndResourceTypeAndResourceIdAndMembershipRoleAndStatus(
        UUID userId, String resourceType, UUID resourceId,
        MembershipRole role, MembershipStatus status);
}
```

### 3.4 Permission Registry (Static Mapping)

#### `PermissionRegistry.java`
Classe statique contenant les deux matrices de permissions :
- `Map<GlobalRoleType, Set<PermissionType>> GLOBAL_ROLE_PERMISSIONS`
- `Map<MembershipRole, Set<PermissionType>> MEMBERSHIP_ROLE_PERMISSIONS`

Méthodes utilitaires :
- `getPermissionsForGlobalRole(GlobalRoleType) → Set<PermissionType>`
- `getPermissionsForMembershipRole(MembershipRole) → Set<PermissionType>`
- `doesGlobalRoleGrant(GlobalRoleType, PermissionType) → boolean`
- `doesMembershipRoleGrant(MembershipRole, PermissionType) → boolean`

### 3.5 Authorization Service (Contrat Central)

#### `AuthorizationService.java` (Interface)
```java
public interface AuthorizationService {
    // Global permission check (no context)
    boolean hasGlobalPermission(UUID userId, PermissionType permission);
    
    // Contextual permission check (within a specific resource)
    boolean hasPermission(UUID userId, PermissionType permission, ResourceContext context);
    
    // Assertion variants (throw ForbiddenException if denied)
    void requireGlobalPermission(UUID userId, PermissionType permission);
    void requirePermission(UUID userId, PermissionType permission, ResourceContext context);
    
    // Admin check
    boolean isAdmin(UUID userId);
}
```

#### `DefaultAuthorizationService.java` (Implementation)
Implements the 6-step decision pipeline documented in section 1.

### 3.6 Spring Security Integration

#### `SecurityAuthzEvaluator.java` (SpEL Bean `@authz`)
A Spring `@Component("authz")` bean exposing methods callable from `@PreAuthorize` annotations :

```java
@Component("authz")
public class SecurityAuthzEvaluator {
    // Called from @PreAuthorize("@authz.hasPermission(principal, 'MOSQUE_UPDATE', 'MOSQUE', #mosqueId)")
    public boolean hasPermission(UserPrincipal principal, String permission, String resourceType, UUID resourceId);
    
    // Called from @PreAuthorize("@authz.hasGlobalPermission(principal, 'DONATION_CREATE')")
    public boolean hasGlobalPermission(UserPrincipal principal, String permission);
    
    // Called from @PreAuthorize("@authz.isAdmin(principal)")
    public boolean isAdmin(UserPrincipal principal);
}
```

### 3.7 Membership Service

#### `MembershipService.java`
Service applicatif de gestion des appartenances :
```java
- assignMembership(userId, resourceType, resourceId, membershipRole, assignedBy) → Membership
- revokeMembership(membershipId, revokedBy)
- getUserMemberships(userId) → List<Membership>
- getResourceMembers(resourceType, resourceId) → List<Membership>
```

### 3.8 Integration with UserPrincipal

`UserPrincipal.fromUser()` sera modifié pour charger dynamiquement les `GlobalRole`s de l'utilisateur depuis la base de données et les injecter comme `GrantedAuthority` Spring Security. Cela permet aux annotations `@PreAuthorize("hasRole('ADMIN')")` de fonctionner nativement.

---

## 4. Flux de Décision d'Autorisation

```
┌─────────────────────────────────────────────────────────────────┐
│  REQUÊTE : userId=U, permission=P, context=R (optionnel)       │
└─────────────┬───────────────────────────────────────────────────┘
              ▼
    ┌─────────────────┐
    │ User U est ACTIVE│──Non──▶ ⛔ REFUSÉ (403)
    │ ?               │
    └────────┬────────┘
             │ Oui
             ▼
    ┌─────────────────┐
    │ U a ROLE_ADMIN ? │──Oui──▶ ✅ AUTORISÉ (audit log)
    └────────┬────────┘
             │ Non
             ▼
    ┌──────────────────────────────┐
    │ P ∈ PermissionRegistry      │
    │   .getPermsForGlobalRoles   │──Oui──▶ ✅ AUTORISÉ
    │   (U.globalRoles)?          │
    └────────┬─────────────────────┘
             │ Non
             ▼
    ┌─────────────────┐
    │ Contexte R      │──Non──▶ ⛔ REFUSÉ (403)
    │ fourni ?        │
    └────────┬────────┘
             │ Oui
             ▼
    ┌──────────────────────────────────────────────────┐
    │ SELECT memberships WHERE userId=U               │
    │   AND resourceType=R.type AND resourceId=R.id   │
    │   AND status='ACTIVE'                           │
    │   AND (expiresAt IS NULL OR expiresAt > NOW())  │
    └────────┬─────────────────────────────────────────┘
             ▼
    ┌──────────────────────────────────────────┐
    │ Pour chaque Membership trouvé :          │
    │   P ∈ PermissionRegistry                 │
    │     .getPermsForMembershipRole(m.role) ? │──Oui──▶ ✅ AUTORISÉ
    └────────┬─────────────────────────────────┘
             │ Aucun match
             ▼
         ⛔ REFUSÉ (403)
```

---

## 5. Architecture Decision Records (ADRs)

### ADR-012 : Modèle d'Autorisation Hybride Tridimensionnel

**Contexte :** Un RBAC global ne permet pas la gouvernance locale de chaque mosquée. Un modèle purement contextuel ignorerait les droits transversaux (Admin, Donor, Volunteer).

**Décision :** Adoption d'un modèle hybride à trois dimensions :
1. Rôles globaux (USER, VOLUNTEER, DONOR, ADMIN) accordant des permissions transversales.
2. Appartenances contextuelles (Membership) liant un utilisateur à une ressource (mosquée) avec un rôle local (IMAM, COMMITTEE_MEMBER...).
3. Permissions granulaires (PermissionType) évaluées selon le contexte.

**Conséquences :** Isolation stricte entre mosquées. Un Imam de la Mosquée A n'a aucun pouvoir sur la Mosquée B. Extensible vers d'autres ressources (Projects, etc.).

### ADR-013 : Permissions comme Capacités Métier Statiques (Enum + Registry In-Memory)

**Contexte :** Stocker les permissions en base de données entraîne des jointures coûteuses à chaque vérification et permet des modifications accidentelles non contrôlées.

**Décision :** Les permissions sont définies comme un `enum PermissionType` Java. Le mapping `Role → Permissions` est codifié dans `PermissionRegistry` (classe statique). Seuls les `Memberships` (qui changent dynamiquement) sont en base.

**Conséquences :** Performance optimale (zero-join pour la résolution des permissions). Sécurité renforcée (impossible de modifier les permissions sans déploiement). Les ajouts de permissions suivent le cycle normal de développement (code review, tests, déploiement).

### ADR-014 : Intégration SpEL Déclarative via `SecurityAuthzEvaluator` (`@authz`)

**Contexte :** Disperser la logique d'autorisation dans les contrôleurs via du code impératif produit du code fragile et non testable.

**Décision :** Un bean Spring `@Component("authz")` expose des méthodes invocables depuis `@PreAuthorize` SpEL. Les contrôleurs déclarent leurs exigences de manière déclarative.

**Conséquences :** Séparation nette contrôleur ↔ autorisation. Testabilité maximale du moteur d'autorisation indépendamment des contrôleurs.

---

## 6. Stratégie de Test

### 6.1 Tests Unitaires

| Classe de Test | Cible | Scénarios |
|---|---|---|
| `PermissionRegistryTest` | `PermissionRegistry` | Cohérence des mappings, ADMIN accorde tout, USER n'accorde pas MOSQUE_UPDATE, chaque MembershipRole accorde les bonnes permissions |
| `MembershipTest` | `Membership` | `isEffective()` avec statuts ACTIVE/REVOKED/EXPIRED, expiration temporelle, `revoke()` |
| `ResourceContextTest` | `ResourceContext` | Factory methods `mosque()`, `project()`, equality/hashcode |
| `DefaultAuthorizationServiceTest` | `DefaultAuthorizationService` (mocked repos) | Pipeline complet : user inactif refusé, admin bypass, global perms, contextual perms, deny by default |

### 6.2 Tests d'Intégration (Isolation Inter-Mosquées — **Exigence Critique**)

| Classe de Test | Scénarios |
|---|---|
| `ContextualAuthorizationIntegrationTest` | **Imam A ↔ Mosque A / Imam B ↔ Mosque B** : A peut gérer A, A ne peut PAS gérer B, B peut gérer B, B ne peut PAS gérer A |
| | Multi-membership : utilisateur Imam de Mosque A ET Committee Member de Mosque B |
| | Membership PENDING → refusé |
| | Membership REVOKED → refusé |
| | Membership expiré → refusé |
| | Utilisateur SUSPENDED → refusé pour tout |
| | ADMIN → autorisé partout |
| | Utilisateur sans rôle → refusé |
| | Permission globale (DONATION_CREATE par DONOR) → autorisé sans contexte |
| | Permission contextuelle (MOSQUE_UPDATE) → refusé sans contexte |

### 6.3 Test `SecurityAuthzEvaluator`

| Scénarios |
|---|
| `hasPermission(principal, "MOSQUE_UPDATE", "MOSQUE", mosqueId)` → true si Imam actif |
| `hasPermission(principal, "MOSQUE_UPDATE", "MOSQUE", otherMosqueId)` → false |
| `isAdmin(principal)` → true/false selon rôle global |

---

## 7. Fichiers Créés / Modifiés

### Nouveaux fichiers (20)

| # | Fichier | Description |
|---|---|---|
| 1 | `backend/src/main/resources/db/migration/V2__init_authorization_schema.sql` | Schéma Flyway : tables `global_roles`, `user_global_roles`, `memberships` + seed |
| 2 | `backend/src/main/java/com/mohsinon/core/security/authorization/GlobalRoleType.java` | Enum des rôles globaux |
| 3 | `backend/src/main/java/com/mohsinon/core/security/authorization/MembershipRole.java` | Enum des rôles de membership |
| 4 | `backend/src/main/java/com/mohsinon/core/security/authorization/MembershipStatus.java` | Enum des statuts de membership |
| 5 | `backend/src/main/java/com/mohsinon/core/security/authorization/PermissionType.java` | Enum des capacités métier |
| 6 | `backend/src/main/java/com/mohsinon/core/security/authorization/ResourceContext.java` | Value Object (type + id) |
| 7 | `backend/src/main/java/com/mohsinon/core/security/authorization/PermissionRegistry.java` | Mapping statique rôles → permissions |
| 8 | `backend/src/main/java/com/mohsinon/core/security/authorization/GlobalRole.java` | Entité JPA |
| 9 | `backend/src/main/java/com/mohsinon/core/security/authorization/Membership.java` | Entité JPA |
| 10 | `backend/src/main/java/com/mohsinon/core/security/authorization/GlobalRoleRepository.java` | Repository JPA |
| 11 | `backend/src/main/java/com/mohsinon/core/security/authorization/UserGlobalRoleRepository.java` | Repository pour jointure user ↔ roles |
| 12 | `backend/src/main/java/com/mohsinon/core/security/authorization/MembershipRepository.java` | Repository JPA |
| 13 | `backend/src/main/java/com/mohsinon/core/security/authorization/AuthorizationService.java` | Interface contrat central |
| 14 | `backend/src/main/java/com/mohsinon/core/security/authorization/DefaultAuthorizationService.java` | Implémentation du pipeline |
| 15 | `backend/src/main/java/com/mohsinon/core/security/authorization/MembershipService.java` | Service de gestion des memberships |
| 16 | `backend/src/main/java/com/mohsinon/core/security/authorization/SecurityAuthzEvaluator.java` | Bean SpEL `@authz` |
| 17 | `backend/src/test/java/.../authorization/PermissionRegistryTest.java` | Tests unitaires du registre |
| 18 | `backend/src/test/java/.../authorization/MembershipTest.java` | Tests unitaires de l'entité |
| 19 | `backend/src/test/java/.../authorization/DefaultAuthorizationServiceTest.java` | Tests unitaires du pipeline (mocks) |
| 20 | `backend/src/test/java/.../authorization/ContextualAuthorizationIntegrationTest.java` | Tests d'intégration inter-mosquées |

### Fichiers modifiés (6)

| # | Fichier | Modification |
|---|---|---|
| 1 | `backend/src/main/java/com/mohsinon/core/security/UserPrincipal.java` | `fromUser()` chargera les `GlobalRole`s depuis la base |
| 2 | `backend/src/main/java/com/mohsinon/core/security/JwtAuthenticationFilter.java` | Chargement des authorities depuis les rôles globaux lors de la reconstruction du principal |
| 3 | `ARCHITECTURE.md` | Section Authorization Engine ajoutée |
| 4 | `DECISIONS.md` | ADR-012, ADR-013, ADR-014 |
| 5 | `PROJECT_STATUS.md` | Milestone 3 marqué terminé |
| 6 | `CHANGELOG.md` | Version `0.4.0-alpha` |

### Nouveau fichier documentation (1)

| # | Fichier |
|---|---|
| 1 | `docs/daily/2026-08-17-milestone-3-authorization.md` |

---

## 8. Vérification de Conformité

| Document | Conformité |
|---|---|
| `VISION.md` | ✅ Gouvernance locale autonome de chaque mosquée via Memberships contextuels |
| `ARCHITECTURE.md` | ✅ Core-First, DDD, Clean Architecture, pas de couplage entre modules |
| `REQUIREMENTS.md` | ✅ REQ-IAM-04 (RBAC & Permissions), REQ-MSQ-02 (Gouvernance locale Imam + Comité) |
| `DECISIONS.md` | ✅ Extension naturelle d'ADR-001 (Modular Monolith), ADR-002 (Core-First), ADR-003 (Modèle d'Autorisation Découplé) |

---

## 9. Ordre d'Implémentation

1. Enums (`GlobalRoleType`, `MembershipRole`, `MembershipStatus`, `PermissionType`)
2. `ResourceContext` Value Object
3. `PermissionRegistry` + `PermissionRegistryTest`
4. `GlobalRole` Entity + `GlobalRoleRepository`
5. `Membership` Entity + `MembershipTest` + `MembershipRepository`
6. Flyway `V2__init_authorization_schema.sql`
7. `UserGlobalRoleRepository` (ou intégration JPQL)
8. `AuthorizationService` interface + `DefaultAuthorizationService` + `DefaultAuthorizationServiceTest`
9. `MembershipService`
10. `SecurityAuthzEvaluator`
11. Modification `UserPrincipal` + `JwtAuthenticationFilter`
12. `ContextualAuthorizationIntegrationTest`
13. Documentation & commit
