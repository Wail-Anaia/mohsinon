# ARCHITECTURE DU SYSTÈME — MOHSINON

## 1. Principes Directeurs

Mohsinon est conçu pour devenir une infrastructure numérique mondiale capable de servir des millions d'utilisateurs tout en restant maintenable, testable et évolutive par une équipe à taille humaine.

### Principes Clés :
1. **Modular Monolith** : Un déploiement unifié avec des frontières de domaine strictement isolées. Pas de complexité opérationnelle prématurée liée aux microservices.
2. **Core-First Architecture** : Un noyau partagé (`com.mohsinon.core`) contenant les services transversaux stables (sécurité, audit, exceptions, abstractions géographiques, entités de base, pagination).
3. **Domain-Driven Design (DDD)** : Chaque module métier modélise son contexte délimité (*Bounded Context*) avec son langage ubiquitaire propre, ses entités, ses value objects, ses repositories et ses services d'application.
4. **Dependency Inversion & Clean Architecture** : La logique de domaine est indépendante des frameworks, de l'infrastructure et de l'interface utilisateur.
5. **Préparation à l'Évolution** : Aucun couplage direct entre modules métier via des tables ou des classes internes. Les communications inter-modules s'effectuent par interfaces explicites ou événements de domaine (`Domain Events`).

---

## 2. Décomposition Modulaire

```text
com.mohsinon
├── MohsinonApplication.java
│
├── core/                                    # SOCLE TRANSVERSAL PARTAGÉ
│   ├── config/                              # Configuration Spring (Security, Web, OpenAPI, JPA)
│   ├── domain/                              # BaseEntity, Value Objects génériques (GeoLocation, Address)
│   ├── security/                            # JWT Filter, TokenProvider, UserPrincipal, Method Security
│   ├── audit/                               # AuditListener, AuditLog, AuditAction
│   ├── exception/                           # BusinessException, ResourceNotFoundException, GlobalExceptionHandler
│   └── pagination/                          # PageResponse, FilterRequest, SortCriteria
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
    └── reputation/                          # Moteur d'Impact, Points & Badges de Contribution
        ├── domain/                          # ImpactPoint, ContributionScore, Badge, Achievement
        ├── repository/                      # ImpactRepository, BadgeRepository
        ├── service/                         # ReputationService, ImpactEvaluationService
        └── web/                             # ReputationController (DTOs v1)
```

---

## 3. Modèle d'Autorisation Évolutif (Decoupled RBAC + Contextual Access)

Le système d'autorisation de Mohsinon distingue explicitement :
- **L'Identité** (`User`) : Le compte authentifié global.
- **Les Rôles Système** (`Role`) : Rôles d'accès globaux (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`).
- **Les Permissions Granulaires** (`Permission`) : Droits unitaires (`MOSQUE_CREATE`, `MOSQUE_VERIFY`, `DONATION_MANAGE`, `INITIATIVE_APPROVE`, `PROJECT_MANAGE`).
- **Les Positions / Appartenances Contextuelles** (`MosqueMember` / `ProjectParticipant`) : Rôle au sein d'une entité spécifique (ex: Imam de la Mosquée A, Trésorier du Comité de la Mosquée B, Chef de Projet du Projet C).

Cette séparation évite le piège des « super-pouvoirs » rigides et permet d'accorder des droits d'administration locale sans impacter le reste de la plateforme.

---

## 4. Stratégie de Géolocalisation & Respect de la Vie Privée

Pour concilier la recherche de proximité et la protection de la vie privée :
1. **Abstraction `GeoLocation`** : Value object encapsulant la latitude et la longitude avec méthode de calcul de distance (Formule de Haversine).
2. **Confidentialité par Défaut** :
   - Les coordonnées exactes des mosquées publiques sont visibles.
   - Les coordonnées des domiciles des particuliers ne sont **JAMAIS** exposées publiquement ; seules la commune, la zone approximative (rayon flouté) ou le quartier sont partagés.
3. **Transition PostGIS** : L'abstraction permet de passer d'un calcul Haversine Java/H2 en développement à des index spatiaux PostGIS natifs (`ST_DWithin`, `ST_Distance`) en production sans modifier les règles métier.

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

---

## 6. Observabilité, Audit & Sécurité

- **Sécurité Stateless** : Jetons JWT Access Tokens à courte durée de vie (15 min) et Refresh Tokens stockés de manière sécurisée avec rotation automatique et révocation instantanée.
- **Audit Log** : Chaque action critique (`USER_CREATED`, `LOGIN`, `LOGIN_FAILED`, `MOSQUE_CREATED`, `DONATION_CLAIMED`, `INITIATIVE_APPROVED`) est journalisée avec l'identifiant de l'auteur, l'horodatage, l'adresse IP et la ressource modifiée.
- **Gestion Unifiée des Erreurs** : Réponses JSON conformes au standard **RFC 7807 (Problem Details for HTTP APIs)**.
