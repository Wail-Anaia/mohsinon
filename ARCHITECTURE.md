# ARCHITECTURE DU SYSTÈME — MOHSINON

## 1. Principes Directeurs

Mohsinon est conçu pour devenir une infrastructure numérique mondiale capable de servir des millions d'utilisateurs tout en restant maintenable, testable et évolutive par une équipe à taille humaine.

### Principes Clés :
1. **Modular Monolith** : Un déploiement unifié avec des frontières de domaine strictement isolées. Pas de complexité opérationnelle prématurée liée aux microservices.
2. **Core-First Architecture** : Un noyau partagé (`com.mohsinon.core`) contenant les services transversaux stables (sécurité, audit, exceptions RFC 7807, abstractions géographiques, entités de base UUID, pagination, registre d'impact).
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
│   └── web/                                 # CoreHealthController (/api/v1/health)
│
└── modules/                                 # CONTEXTES MÉTIER DÉLIMITÉS (BOUNDED CONTEXTS)
    ├── identity/                            # Authentification, Utilisateurs, RBAC (Rôles & Permissions)
    │   ├── domain/                          # User, Role, Permission, RefreshToken
    │   ├── repository/                      # UserRepository, RoleRepository, RefreshTokenRepository
    │   ├── service/                         # AuthService, UserService, TokenService
    │   └── web/                             # AuthController, UserController (DTOs v1)
    │
    ├── mosques/                             # Hubs Locaux, Imams, Comités de Mosquée, Validation
    │   ├── domain/                          # Mosque, MosqueCommittee, MosqueMember, MosqueVerification
    │   ├── repository/                      # MosqueRepository, MosqueMemberRepository
    │   ├── service/                         # MosqueService, MosqueSearchService (Proximité)
    │   └── web/                             # MosqueController (DTOs v1)
    │
    ├── donations/                           # Bourse d'Entraide Multi-Ressources
    │   ├── domain/                          # DonationItem, DonationCategory, DonationStatus
    │   ├── repository/                      # DonationRepository
    │   ├── service/                         # DonationService
    │   └── web/                             # DonationController (DTOs v1)
    │
    ├── skills/                              # Référentiel de Compétences & Bénévolat
    │   ├── domain/                          # Skill, UserSkill, SkillLevel, VolunteerProfile
    │   ├── repository/                      # SkillRepository, UserSkillRepository
    │   ├── service/                         # SkillService, VolunteerMatchingService
    │   └── web/                             # SkillController, VolunteerController (DTOs v1)
    │
    ├── initiatives/                         # Besoins, Idées, Projets Locaux & Jalons
    │   ├── domain/                          # Initiative, Project, Milestone, Participant, Evidence
    │   ├── repository/                      # InitiativeRepository, ProjectRepository
    │   ├── service/                         # InitiativeService, ProjectExecutionService
    │   └── web/                             # InitiativeController, ProjectController (DTOs v1)
    │
    └── reputation/                          # Moteur d'Impact, Niveaux & Badges de Contribution
        ├── domain/                          # Badge, Achievement, ContributionLevel
        ├── repository/                      # BadgeRepository, AchievementRepository
        ├── service/                         # ReputationEvaluationService
        └── web/                             # ReputationController (DTOs v1)
```

---

## 3. Socle du Core (`com.mohsinon.core`)

### 3.1 BaseEntity
Toutes les entités du domaine dérivent de `BaseEntity` :
- `UUID id` : Clé primaire standard non prédictible.
- `Instant createdAt` : Horodatage d'audit immuable.
- `Instant updatedAt` : Horodatage de dernière modification.
- `Long version` : Verrouillage optimiste (`@Version`) garantissant la cohérence en environnement concurrent.

### 3.2 Abstraction Géographique `GeoLocation`
- Value Object `@Embeddable` encapsulant latitude, longitude, ville et code pays.
- Calcul de distance géodésique via la formule de **Haversine** intégrée (`distanceToInKm`).
- Méthode `toApproximate()` garantissant le floutage des coordonnées de résidences privées pour préserver la vie privée des membres (Privacy-by-Design).
- Prêt pour la transition vers les index spatiaux PostGIS natifs (`GIST`) sans impacter les contrats d'application.

### 3.3 Registre d'Impact Immuable (`ImpactTransaction`)
- Système de ledger comptabilisant chaque crédit ou débit de points d'impact.
- Chaque transaction associe un `userId`, un type (`EARNED`, `SPENT`, `ADJUSTED`), le nombre de `points`, le type de référence (`DONATION`, `VOLUNTEERING`, `PROJECT_CONTRIBUTION`), l'ID de référence et le motif vérifié.
- Le solde total est garanti par recalcul ou matérialisation auditable.

### 3.4 Format Unifié des Erreurs (RFC 7807)
- `GlobalExceptionHandler` intercepte toutes les exceptions métier (`ResourceNotFoundException`, `ConflictException`, `ForbiddenException`, `ValidationException`, etc.) et génère des réponses conformes à **RFC 7807 (Problem Details for HTTP APIs)** :
  ```json
  {
    "type": "https://mohsinon.org/errors/resource_not_found",
    "title": "Resource Not Found",
    "status": 404,
    "detail": "Mosque with id '00000000-0000-0000-0000-000000000001' was not found.",
    "errorCode": "RESOURCE_NOT_FOUND",
    "timestamp": "2026-08-15T00:48:00Z"
  }
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
