# FEUILLE DE ROUTE (ROADMAP) — MOHSINON

La feuille de route suit strictement le principe :  
**BUILD SMALL → VALIDATE → DOCUMENT → EXPAND**

---

## 🎯 Phase 1 : Le MVP Fondateur (Milestones 0 à 9)

```mermaid
flowchart LR
    M0[M0: Foundation] --> M1[M1: Core & Config]
    M1 --> M2[M2: Auth & JWT]
    M2 --> M3[M3: RBAC & Perms]
    M3 --> M4[M4: Users & Profiles]
    M4 --> M5[M5: Mosques & Hubs]
    M5 --> M6[M6: Donations]
    M6 --> M7[M7: Volunteers & Skills]
    M7 --> M8[M8: Initiatives & Projects]
    M8 --> M9[M9: Reputation & Badges]
```

---

### 🔹 Milestone 0 : Foundation, Documentation & Repository
- [x] Initialisation du dépôt Git avec `.gitignore` universel.
- [x] Création des 9 documents de référence et de la baseline technique.
- [x] Mise en place de l'arborescence documentaire (`docs/daily/`, `docs/architecture/`, etc.).
- [x] Squelette de projet Backend (Spring Boot 3 / Java 17) et Frontend (Angular 18+).

### 🔹 Milestone 1 : Core Foundation & Infrastructure
- [x] Configuration Spring Boot 3.x, profils `dev` (H2-PostgreSQL) et `prod` (PostgreSQL/PostGIS).
- [x] Entités de base `BaseEntity` (id, timestamps d'audit `createdAt`/`updatedAt`, `version` optimistic locking).
- [x] Gestion centralisée des exceptions conforme **RFC 7807 (Problem Details)**.
- [x] Structure de pagination & filtrage standardisée (`PageResponse`, `FilterCriteria`).
- [x] Abstraction de géolocalisation `GeoLocation` (calcul Haversine découplé).
- [x] Configuration Flyway initiale (Migration V1).

### 🔹 Milestone 2 : Identity & Authentication (JWT & Refresh Tokens)
- [x] Entités `User`, `RefreshToken` (stockage SHA-256).
- [x] Services `AuthService`, `TokenProvider` (JJWT 0.12+).
- [x] Rotation automatique et révocation des Refresh Tokens avec détection de fraude.
- [x] Filtre de sécurité JWT (`JwtAuthenticationFilter`) & Endpoints `/api/v1/auth/login`, `/register`, `/refresh`, `/logout`, `/me`.
- [x] Tests unitaires et d'intégration de sécurité (41 tests).

### 🔹 Milestone 3 : Authorization, Roles & Permissions (Gouvernance Locale Tridimensionnelle)
- [x] Modèle tridimensionnel : Rôle Global $\neq$ Permission $\neq$ Membership $\neq$ ResourceContext.
- [x] Registre statique `PermissionRegistry` in-memory (zéro jointure SQL).
- [x] Entités `GlobalRole`, `UserGlobalRole`, `Membership` (avec cycle de vie et expiration).
- [x] Moteur d'autorisation `AuthorizationService` (pipeline Deny-by-Default en 6 étapes).
- [x] Gestionnaire de positions locales `MembershipService`.
- [x] Évaluateur Spring Security SpEL `@authz` (`SecurityAuthzEvaluator`).
- [x] Migration Flyway V2 (`V2__init_authorization_schema.sql`) validée sur PostgreSQL 18.2.
- [x] Tests d'étanchéité et preuve formelle d'isolation cross-mosque (110 tests passés).

### 🔹 Milestone 4 : Users & Profiles
- [ ] Profils utilisateurs enrichis (bio, préférences de notification, ville, langues).
- [ ] Protection stricte de la vie privée (aucune donnée de localisation fine exposée).
- [ ] DTOs de consultation publique vs consultation privée.
- [ ] Tests du cycle de vie du profil.

### 🔹 Milestone 5 : Mosques, Imam & Mosque Committee
- [ ] Entités `Mosque`, `MosqueMember`, `MosqueRole` (`IMAM`, `COMMITTEE_PRESIDENT`, `TREASURER`, `MEMBER`).
- [ ] Recherche de mosquées (nom, ville, rayon de proximité kilométrique).
- [ ] Processus de vérification des mosquées (`PENDING`, `VERIFIED`).
- [ ] Espace d'administration locale pour l'Imam et le Comité.

### 🔹 Milestone 6 : Multi-Resource Donations
- [ ] Entité générique `DonationItem` et catégories : `MONEY`, `CLOTHES`, `FOOD`, `FURNITURE`, `BOOKS`, `EQUIPMENT`, `MATERIAL`, `SERVICE`, `TIME`.
- [ ] Cycle d'attribution et de remise de don.
- [ ] Point relais à la mosquée ou échange direct.
- [ ] Validation de réception et traçabilité.

### 🔹 Milestone 7 : Volunteers & Skills
- [ ] Taxonomie des compétences (`Skill`, `UserSkill`, niveaux de `BEGINNER` à `MASTER`).
- [ ] Profil de disponibilité bénévole.
- [ ] Moteur de mise en correspondance (matching) entre besoins locaux et compétences disponibles.

### 🔹 Milestone 8 : Initiatives & Projects
- [ ] Cycle de vie complet : Besoin/Idée → Validation locale (Mosquée/Modérateur) → Projet → Jalons (`Milestones`) → Exécution.
- [ ] Gestion des participants et des tâches.
- [ ] Enregistrement des preuves d'impact (comptes-rendus, photos vérifiées).

### 🔹 Milestone 9 : Reputation, Impact Points & Badges
- [ ] Système de points d'impact (`Impact Points`) basé sur des actions réelles vérifiées.
- [ ] Attribution de badges de reconnaissance communautaire.
- [ ] Historique d'impact auditable et profil public de contribution.
- [ ] Préparation du concept d'unité de contribution interne (sans cryptomonnaie réelle).

---

## 🚀 Phase 2 : Extensions & Échelle Mondiale (Post-MVP)

- **Module Education & Mentorat** : Partage de savoirs, cours communautaires, mentorat structuré.
- **Module Marketplace Solidaire** : Échanges de biens sans monnaie spéculative.
- **Module Insertion & Emploi** : Connexion entre talents locaux et opportunités professionnelles.
- **Gouvernance Décentralisée** : Conseils communautaires, votes transparents et mandats audités.
- **Mohsinon AI** : Détection des doublons d'idées, recommandations d'entraide, assistance au cadrage de projets.
