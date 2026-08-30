# JOURNAL DES MODIFICATIONS (CHANGELOG) — MOHSINON

Toutes les modifications notables apportées au projet Mohsinon sont consignées dans ce document.  
Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.0.0/), et ce projet adhère à [Semantic Versioning](https://semver.org/lang/fr/).

---

## [0.4.0-alpha] - 2026-08-30

### Ajouté (Milestone 3 : Authorization & Contextual Governance — Clôturé)
- **Modèle Tridimensionnel d'Autorisation** : Découplage strict entre Rôles Globaux (`GlobalRoleType`), Positions Locales (`MembershipRole`), Permissions Granulaires (`PermissionType`), et Contexte (`ResourceContext`).
- **ResourceContext Value Object** : Encapsulation immuable `(resourceType, resourceId)` avec constructeurs typés (`ResourceContext.mosque(id)`).
- **PermissionRegistry In-Memory** : Registre statique immuable résolvant les permissions sans jointure SQL (ADR-013).
- **Entités JPA & Persistance** : `GlobalRole`, `UserGlobalRole`, `Membership` (avec statuts `ACTIVE`, `PENDING_APPROVAL`, `REVOKED`, `EXPIRED`, audit `assignedBy` et date de fin optionnelle).
- **Moteur d'Autorisation (`AuthorizationService` / `DefaultAuthorizationService`)** : Pipeline Deny-by-Default en 6 étapes garantissant l'isolation inter-mosquées et le bypass universel pour `ROLE_ADMIN` (ADR-012).
- **MembershipService** : Gestion du cycle de vie des appartenances et positions locales (Imam, Président, Trésorier, etc.).
- **Évaluateur Spring Security SpEL (`SecurityAuthzEvaluator` / `@authz`)** : Composant `@Component("authz")` pour annotations déclaratives `@PreAuthorize`.
- **Intégration UserPrincipal** : Chargement dynamique des rôles globaux dans le contexte de sécurité via `JwtAuthenticationFilter`.
- **Flyway V2** : Script de migration SQL `V2__init_authorization_schema.sql` validé sur serveur réel PostgreSQL 18.2 (`mohsinon_db`) avec `ddl-auto: validate`.
- **Tests & Validation** : 110 tests unitaires et d'intégration validés sans erreur (`mvn clean test`), incluant la preuve formelle d'isolation cross-mosque, l'étanchéité inter-ressources et la frontière d'authentification 401/403.

---

## [0.3.0-alpha] - 2026-08-15

### Ajouté (Milestone 2 : Identity & Authentication)
- **Modèle User** : Entité `User` (`UUID id`, `username`, `email`, `passwordHash`, `firstName`, `lastName`, `displayName`, `status`).
- **UserStatus** : Enum de cycle de vie de compte (`ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`).
- **RefreshToken & Sécurité** : Entité `RefreshToken` avec stockage haché (SHA-256), rotation atomique et détection de réutilisation/fraude invalidant toutes les sessions (ADR-010).
- **JWT & TokenProvider** : Émission et validation de jetons JWT Access Token signés HMAC-SHA-256 (JJWT 0.12) et génération de refresh tokens sécurisés.
- **Filtres de Sécurité** : `JwtAuthenticationFilter`, `JwtAuthenticationEntryPoint` (formatant les 401 en RFC 7807 ProblemDetail).
- **Abstraction CurrentUserProvider** : Découplage de `SecurityContextHolder` via `CurrentUserProvider` / `SecurityContextCurrentUserProvider` (ADR-011).
- **Service d'Authentification** : `AuthService` gérant inscription, connexion, rafraîchissement avec rotation, déconnexion et profil.
- **API REST `/api/v1/auth/*`** : Endpoints `/register`, `/login`, `/refresh`, `/logout`, `/me`.
- **Flyway V1** : Script de migration SQL `V1__init_identity_schema.sql`.
- **Tests** : 41 tests unitaires et d'intégration validés sans erreur.

---

## [0.2.0-alpha] - 2026-08-15

### Ajouté (Milestone 1 : Core Foundation & Infrastructure)
- **BaseEntity** : Entité abstraite avec `UUID`, timestamps et verrouillage optimiste.
- **GeoLocation** : Value Object `@Embeddable` avec calcul Haversine et floutage de coordonnées.
- **RFC 7807 Problem Details** : `GlobalExceptionHandler` et hiérarchie `BusinessException`.
- **Pagination & Sorting** : DTOs génériques (`PageResponse<T>`, `PaginationRequest`).
- **Impact Transaction Ledger** : Modèle immuable `ImpactTransaction` et repository de calcul de solde.
- **Sécurité Core & Configuration** : `CoreSecurityConfig`, `JpaConfig`, `OpenApiConfig`, `/api/v1/health`.

---

## [0.1.0-alpha] - 2026-08-15

### Ajouté (Milestone 0 : Foundation, Documentation & Repository)
- Initialisation Git, `.gitignore`, suite documentaire complète et arborescence `docs/`.
