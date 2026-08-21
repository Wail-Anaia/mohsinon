# ARCHITECTURE DU SYSTÈME — MOHSINON

## 1. Principes Directeurs

Mohsinon est conçu pour devenir une infrastructure numérique mondiale capable de servir des millions d'utilisateurs tout en restant maintenable, testable et évolutive par une équipe à taille humaine.

### Principes Clés :
1. **Modular Monolith** : Un déploiement unifié avec des frontières de domaine strictement isolées. Pas de complexité opérationnelle prématurée liée aux microservices.
2. **Core-First Architecture** : Un noyau partagé (`com.mohsinon.core`) contenant les services transversaux stables (sécurité, audit, exceptions RFC 7807, abstractions géographiques, entités de base UUID, pagination, registre d'impact, abstraction `CurrentUserProvider`, moteur d'autorisation).
3. **Domain-Driven Design (DDD)** : Chaque module métier modélise son contexte délimité (*Bounded Context*) avec son langage ubiquitaire propre, ses entités, ses value objects, ses repositories et ses services d'application.
4. **Dependency Inversion & Clean Architecture** : La logique de domaine est indépendante des frameworks, de l'infrastructure et de l'interface utilisateur.
5. **Préparation à l'Évolution** : Aucun couplage direct entre modules métier via des tables ou des classes internes. Les communications inter-modules s'effectuent par interfaces explicites ou événements de domaine (`Domain Events`).
6. **Identifiants Standardisés** : Utilisation exclusive d'identifiants `UUID` (RFC 4122) pour toutes les entités (`BaseEntity`), prévenant l'énumération non sécurisée et facilitant le sharding futur.

---

## 2. Décomposition Modulaire

```text
com.mohsinon
├── MohsinonApplication.java
│
├── core/                                    # SOCLE TRANSVERSAL PARTAGÉ
│   ├── config/                              # Configuration Spring (CoreSecurityConfig, JpaConfig, OpenApiConfig)
│   ├── domain/                              # BaseEntity (UUID, timestamps, version), GeoLocation Value Object
│   ├── reputation/                          # ImpactTransaction (Ledger d'impact immuable), ImpactTransactionRepository
│   ├── exception/                           # BusinessException, ResourceNotFound, Conflict, Forbidden, GlobalExceptionHandler (RFC 7807)
│   ├── pagination/                          # PageResponse<T>, PaginationRequest, SortDirection
│   ├── security/                            # JwtAuthenticationFilter, JwtAuthenticationEntryPoint, TokenProvider, CurrentUserProvider
│   │   └── authorization/                   # MOTEUR D'AUTORISATION TRIDIMENSIONNEL (Milestone 3)
│   │       ├── model/                       # GlobalRoleType, MembershipRole, MembershipStatus, PermissionType, ResourceContext, PermissionRegistry
│   │       ├── entity/                      # GlobalRole, UserGlobalRole, Membership
│   │       ├── repository/                  # GlobalRoleRepository, UserGlobalRoleRepository, MembershipRepository
│   │       ├── service/                     # AuthorizationService, DefaultAuthorizationService, MembershipService
│   │       └── evaluator/                   # SecurityAuthzEvaluator (alias SpEL @authz)
│   └── web/                                 # CoreHealthController (/api/v1/health)
│
└── modules/                                 # CONTEXTES MÉTIER DÉLIMITÉS (BOUNDED CONTEXTS)
    ├── identity/                            # Authentification, Utilisateurs, Cycle de vie des Sessions
    │   ├── domain/                          # User, UserStatus, RefreshToken
    │   ├── repository/                      # UserRepository, RefreshTokenRepository
    │   ├── service/                         # AuthService
    │   └── web/                             # AuthController (/api/v1/auth/register, /login, /refresh, /logout, /me)
    │
    ├── mosques/                             # Hubs Locaux, Imams, Comités de Mosquée, Validation (Milestone 5)
    ├── donations/                           # Bourse d'Entraide Multi-Ressources (Milestone 6)
    ├── skills/                              # Référentiel de Compétences & Bénévolat (Milestone 7)
    ├── initiatives/                         # Besoins, Idées, Projets Locaux & Jalons (Milestone 8)
    └── reputation/                          # Moteur d'Impact, Niveaux & Badges de Contribution (Milestone 9)
```

---

## 3. Moteur d'Autorisation Tridimensionnel (Milestone 3)

### 3.1 Découplage Fondamental
```text
Rôle Global (Qui vous êtes) 
   ≠ Permission (Ce que vous pouvez faire) 
   ≠ Membership (Où et quelle position vous occupez) 
   ≠ ResourceContext (Ressource précise protégée)
```

### 3.2 Pipeline d'Évaluation (Deny by Default)
```text
Demande d'Autorisation : (userId, permission, context)
  │
  ├── 1. Statut Utilisateur == ACTIVE ? (Sinon -> DENY 401/403)
  ├── 2. Utilisateur a ROLE_ADMIN ? (Si OUI -> ALLOW avec audit)
  ├── 3. Permission accordée globalement via GlobalRole ? (Si OUI -> ALLOW)
  ├── 4. ResourceContext fourni ? (Si NON -> DENY)
  ├── 5. Membership actif et non expiré sur (resourceType, resourceId) accorde la permission ? (Si OUI -> ALLOW)
  └── 6. DENY par défaut (403 Forbidden)
```

### 3.3 Isolation Inter-Mosquées Prouvée
- **Imam A sur Mosquée A** $\rightarrow$ `canManageMosque(Imam A, Mosque A)` = **AUTORISÉ**
- **Imam A sur Mosquée B** $\rightarrow$ `canManageMosque(Imam A, Mosque B)` = **REFUSÉ** (403)
- **Imam B sur Mosquée B** $\rightarrow$ `canManageMosque(Imam B, Mosque B)` = **AUTORISÉ**
- **Imam B sur Mosquée A** $\rightarrow$ `canManageMosque(Imam B, Mosque A)` = **REFUSÉ** (403)

### 3.4 Sécurité Déclarative SpEL (`@authz`)
```java
@PreAuthorize("@authz.canManageMosque(principal, #mosqueId)")
@PutMapping("/api/v1/mosques/{mosqueId}")
public ResponseEntity<MosqueResponse> updateMosque(@PathVariable UUID mosqueId, ...) { ... }
```

---

## 4. Architecture Frontend (Angular Standalone)

L'application web Angular est organisée en couches modulaires et prévisibles :
```text
frontend/src/app/
├── core/                                    # Singleton Services, Interceptors & Guards
│   ├── auth/                                # AuthService, AuthGuard, RoleGuard, TokenStorage
│   └── interceptors/                        # JwtInterceptor, ErrorInterceptor
├── shared/                                  # Composants, Directives (HasPermissionDirective)
├── layouts/                                 # MainLayout, AuthLayout
└── features/                                # Modules Fonctionnels Lazy-Loaded
```
