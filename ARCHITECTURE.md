# ARCHITECTURE DU SYSTÈME — MOHSINON

## 1. Principes Directeurs

Mohsinon est conçu pour devenir une infrastructure numérique mondiale capable de servir des millions d'utilisateurs tout en restant maintenable, testable et évolutive par une équipe à taille humaine.

### Principes Clés :
1. **Modular Monolith** : Un déploiement unifié avec des frontières de domaine strictement isolées. Pas de complexité opérationnelle prématurée liée aux microservices.
2. **Core-First Architecture** : Un noyau partagé (`com.mohsinon.core`) contenant les services transversaux stables (sécurité, audit, exceptions RFC 7807, abstractions géographiques, entités de base UUID, pagination, registre d'impact, abstraction `CurrentUserProvider`).
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
    │   ├── domain/                          # Mosque, MosqueCommittee, MosqueMember, MosqueVerification
    │   ├── repository/                      # MosqueRepository, MosqueMemberRepository
    │   ├── service/                         # MosqueService, MosqueSearchService (Proximité)
    │   └── web/                             # MosqueController (DTOs v1)
    │
    ├── donations/                           # Bourse d'Entraide Multi-Ressources (Milestone 6)
    │   ├── domain/                          # DonationItem, DonationCategory, DonationStatus
    │   ├── repository/                      # DonationRepository
    │   ├── service/                         # DonationService
    │   └── web/                             # DonationController (DTOs v1)
    │
    ├── skills/                              # Référentiel de Compétences & Bénévolat (Milestone 7)
    │   ├── domain/                          # Skill, UserSkill, SkillLevel, VolunteerProfile
    │   ├── repository/                      # SkillRepository, UserSkillRepository
    │   ├── service/                         # SkillService, VolunteerMatchingService
    │   └── web/                             # SkillController, VolunteerController (DTOs v1)
    │
    ├── initiatives/                         # Besoins, Idées, Projets Locaux & Jalons (Milestone 8)
    │   ├── domain/                          # Initiative, Project, Milestone, Participant, Evidence
    │   ├── repository/                      # InitiativeRepository, ProjectRepository
    │   ├── service/                         # InitiativeService, ProjectExecutionService
    │   └── web/                             # InitiativeController, ProjectController (DTOs v1)
    │
    └── reputation/                          # Moteur d'Impact, Niveaux & Badges de Contribution (Milestone 9)
        ├── domain/                          # Badge, Achievement, ContributionLevel
        ├── repository/                      # BadgeRepository, AchievementRepository
        ├── service/                         # ReputationEvaluationService
        └── web/                             # ReputationController (DTOs v1)
```

---

## 3. Module Identity & Authentification (Milestone 2)

### 3.1 Entité `User`
- Clé primaire `UUID id` héritée de `BaseEntity`.
- Unicité stricte garantie en base (`uk_users_email`, `uk_users_username`) et normalisation automatique en minuscules.
- Statut d'authentification explicite (`UserStatus` : `ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`).
- Hachage de mot de passe via `PasswordEncoder` (BCrypt cost factor 12) ; le hash n'est jamais sérialisé vers l'extérieur.

### 3.2 Cycle des Jetons (JWT + Refresh Token Rotation)
```text
Client                              API Backend                               Database
  │                                     │                                         │
  │── POST /api/v1/auth/login ─────────>│                                         │
  │   (email/username + password)       │── Verify password BCrypt ──────────────>│
  │                                     │── Generate JWT Access Token (15 min)    │
  │                                     │── Generate Opaque Refresh Token (7 days)│
  │                                     │── Persist SHA-256(RefreshToken) ───────>│
  │<── Return AccessToken + RefreshToken│                                         │
  │                                     │                                         │
  │── POST /api/v1/auth/refresh ───────>│                                         │
  │   (RefreshToken A)                  │── Hash & Lookup Token A ───────────────>│
  │                                     │   ├── If revoked/reused: REVOKE ALL! ──>│
  │                                     │   └── If valid: Mark A revoked &        │
  │                                     │       generate Token B (linked) ───────>│
  │<── Return new AccessToken + Token B ─│                                        │
```

---

## 4. Modèle d'Autorisation Évolutif (Decoupled RBAC + Contextual Access)

Le système d'autorisation de Mohsinon distingue explicitement :
- **L'Identité** (`User`) : Le compte authentifié global.
- **Les Rôles Système** (`Role`) : Rôles d'accès globaux (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`).
- **Les Permissions Granulaires** (`Permission`) : Droits unitaires (`MOSQUE_CREATE`, `MOSQUE_VERIFY`, `DONATION_MANAGE`, `INITIATIVE_APPROVE`, `PROJECT_MANAGE`).
- **Les Positions / Appartenances Contextuelles** (`MosqueMember` / `ProjectParticipant`) : Rôle au sein d'une entité spécifique (ex: Imam de la Mosquée A, Trésorier du Comité de la Mosquée B, Chef de Projet du Projet C).

---

## 5. Architecture Frontend (Angular Standalone)

L'application web Angular est organisée en couches modulaires et prévisibles :

```text
frontend/src/app/
├── core/                                    # Singleton Services & Interceptors
│   ├── auth/                                # AuthService, AuthGuard, RoleGuard, TokenStorage
│   ├── interceptors/                        # JwtInterceptor, ErrorInterceptor, LoggingInterceptor
│   └── api/                                 # ApiClient, BaseService
│
├── shared/                                  # Composants, Directives & Pipes Réutilisables
│   ├── components/                          # Navbar, Footer, Modal, Toast, Button, Badge, Card, Avatar
│   ├── directives/                          # HasPermissionDirective, ClickOutsideDirective
│   └── pipes/                               # DistanceFormatPipe, RelativeTimePipe, SafeHtmlPipe
│
├── layouts/                                 # Gabarits de Pages
│   ├── main-layout/                         # Header public/connecté, contenu, footer
│   └── auth-layout/                         # Pages de connexion/inscription centrées
│
└── features/                                # Modules Fonctionnels (Lazy Loaded Routes)
    ├── auth/                                # Login, Register, Forgot Password
    ├── mosques/                             # Annuaire, Fiche Mosquée, Administration locale
    ├── donations/                           # Bourse aux dons, Publication, Réservation
    ├── initiatives/                         # Liste des projets, Proposition d'idée, Jalons
    ├── skills/                              # Bourse de compétences, Profil bénévole
    └── profile/                             # Profil personnel, Badges, Points d'impact
```
