# IMPLEMENTATION BASELINE — MOHSINON

**Date :** 15 Août 2026  
**Auteurs :** Équipe d'Ingénierie Mohsinon  
**Statut :** Baseline Initiale — Étape Inception & Cadrage  

---

## 1. Current State (État Actuel)

Le répertoire de travail `c:\Users\Wail Anaia\Desktop\mohsinon` est actuellement vierge (projet greenfield). 

### Inventaire de l'Environnement et des Outils Locaux :
- **Système d'exploitation :** Windows (PowerShell)
- **Runtime Java :** Java 17 LTS disponible (`C:\Program Files\Java\jdk-17\bin\java.exe` — JDK 17.0.12 LTS 64-Bit)
- **Runtime Node.js :** `v24.14.0`
- **Gestionnaire de paquets NPM :** `11.9.0`
- **Contrôle de version Git :** `2.52.0.windows.1` (dépôt non encore initialisé)
- **Docker :** Non présent dans le `PATH` système immédiat (l'environnement de développement local s'appuiera sur un profil `dev` H2 compatible PostgreSQL et des scripts de conteneurisation prêts pour déploiement CI/CD / VPS / Cloud).

---

## 2. Existing Architecture (Architecture Existante)

Aucun composant n'est encore déployé dans le workspace. 
L'architecture cible validée est un **Modular Monolith** structuré selon les principes du **Domain-Driven Design (DDD)** et de la **Clean Architecture**, organisé comme suit :

```text
mohsinon/
├── backend/                  # Modular Monolith Spring Boot 3.x / Java 17
│   ├── src/main/java/com/mohsinon/
│   │   ├── core/             # Cross-cutting (Security, Audit, Exceptions, Base Entities, Geo abstraction)
│   │   └── modules/
│   │       ├── identity/     # Auth, Users, Roles, Permissions, Tokens
│   │       ├── mosques/      # Mosque Hubs, Comités, Imams, Profils, Localisation
│   │       ├── donations/    # Dons multi-ressources (Financier, Matériel, Nourriture, Temps, Services)
│   │       ├── initiatives/  # Besoins communautaires, Idées, Projets, Jalons
│   │       ├── skills/       # Compétences, Bénévoles, Mise en relation
│   │       └── reputation/   # Points d'impact, Historique de contribution, Badges
│   └── src/main/resources/
│       ├── application.yml   # Profils dev (H2-PostgreSQL) et prod (PostgreSQL/PostGIS)
│       └── db/migration/     # Scripts Flyway
├── frontend/                 # Application Web Angular Standalone (Signals, SCSS, Design System)
├── docs/                     # Documentation pérenne & architecture decision records (ADRs)
└── docker-compose.yml        # Configuration d'infrastructure
```

---

## 3. Existing Features (Fonctionnalités Existantes)

- **Actuellement implémentées :** `0%` (Démarrage du projet).

---

## 4. Missing Features (Fonctionnalités Manquantes pour le MVP)

### A. Gouvernance Documentaire & Socle
- [ ] Initialisation du dépôt Git (`.gitignore`, conventions de commits).
- [ ] Suite documentaire de référence (`VISION.md`, `ARCHITECTURE.md`, `REQUIREMENTS.md`, `ROADMAP.md`, `DECISIONS.md`, `DEVELOPMENT_GUIDE.md`, `PROJECT_STATUS.md`, `CHANGELOG.md`, `README.md`, `docs/daily/`).

### B. Module Identity & Access (Sécurité & Utilisateurs)
- [ ] Modèle RBAC découplé : `Role` -> `Permission` -> `Authorization`.
- [ ] Rôles initiaux : `ADMIN`, `IMAM`, `MOSQUE_COMMITTEE`, `VOLUNTEER`, `DONOR`, `USER`.
- [ ] Authentification : Inscription, Connexion, JWT Access Token, Refresh Token Rotation, Révocation.
- [ ] Profils utilisateurs avec préservation stricte de la confidentialité géographique (pas d'adresses exactes exposées).

### C. Module Mosques (Hub Communautaire Local)
- [ ] Entité `Mosque` (Nom, Ville, Quartier, Pays, Coordonnées, Statut de vérification).
- [ ] Rôles associés : Imam référent et membres du Comité de mosquée avec permissions de modération locale.
- [ ] Recherche et annuaire des mosquées (par ville, région, et recherche par proximité géographique via abstraction `Location`).

### D. Module Donations (Dons Multi-Ressources)
- [ ] Modèle générique : Argent, Vêtements, Meubles, Nourriture, Équipement, Livres, Matériel, Services, Temps.
- [ ] Cycle de vie d'un don : Création, Disponibilité, Attribution, Preuve / Validation de réception.

### E. Module Initiatives & Projets
- [ ] Cycle : Besoin communautaire / Idée -> Proposition -> Validation Comité/Imam -> Projet actif -> Équipe -> Jalons -> Preuve d'impact.

### F. Module Skills & Volunteers
- [ ] Référentiel des compétences et niveaux (`Beginner`, `Intermediate`, `Advanced`, `Expert`, `Mentor`, `Master`).
- [ ] Profil bénévole et matching de compétences avec les besoins locaux de la mosquée/projets.

### G. Module Reputation & Impact (Socle initial)
- [ ] Points d'impact (`Impact Points`), badges initiaux vérifiables et journal d'audit des contributions.
- [ ] Préparation du concept d'unité interne de contribution (sans aucune complexité crypto/blockchain prématurée).

---

## 5. Technical Debt (Dette Technique Identifiée)

- **Dette actuelle :** Aucune (projet vierge).
- **Points de vigilance préventifs :**
  - Éviter le couplage fort entre modules dès le démarrage : utilisation stricte d'APIs/interfaces et de DTOs entre modules.
  - Ne pas surcharger l'entité `User` ou créer des `God Services`.
  - Maintenir une séparation nette entre Rôles et Permissions pour permettre des délégations fines (ex: comité de mosquée vs bénévole temporaire).

---

## 6. Conflicts with Target Architecture (Conflits avec l'Architecture Cible)

- Aucun conflit détecté. Le choix du **Modular Monolith** évite la complexité opérationnelle prématurée des microservices tout en garantissant des frontières de domaine nettes pour de futures extractions si nécessaire.

---

## 7. Recommended Refactoring / Inception Strategy

Nous appliquons rigoureusement la méthodologie :  
**BUILD SMALL → VALIDATE → DOCUMENT → EXPAND**

### Découpage en Milestones Séquentiels :

| Milestone | Intitulé | Périmètre & Objectif |
| :--- | :--- | :--- |
| **M0** | **Inception, Docs & Project Skeleton** | Initialisation Git, création des 9 documents de référence (`VISION.md`, `ARCHITECTURE.md`, `ROADMAP.md`, etc.), squelette du projet backend Spring Boot & frontend Angular. |
| **M1** | **Core Foundation & Identity Module** | Socle technique backend (Security JWT, Exception Handling, Audit), entités `User`, `Role`, `Permission`, `RefreshToken`, API `/api/v1/auth`, tests unitaires et d'intégration. |
| **M2** | **Mosques Domain (Local Hubs)** | Module `mosques`, gestion Imam & Comité, profils mosquées, recherche par ville/quartier et abstraction de proximité géographique `GeoLocation`. |
| **M3** | **Donations & Mutual Aid Engine** | Module `donations` multi-types (argent, vivres, vêtements, matériel, compétences, temps), publication et suivi des dons. |
| **M4** | **Initiatives, Projects & Volunteers** | Module `initiatives` & `skills`, soumission d'idées/besoins, validation, équipe de projet, matching compétences/bénévoles. |
| **M5** | **Reputation, Impact Baseline & Frontend Integration** | Points d'impact, historique vérifié, interface web Angular connectée et tests end-to-end du parcours utilisateur. |

---

## 8. Next Milestone (Prochaine Étape Immédiate)

👉 **Milestone 0 : Inception, Dépôt Git & Référentiel Documentaire Complet**
1. Initialisation du dépôt Git avec configuration `.gitignore` standard (Java, Node, IDEs).
2. Rédaction complète et détaillée de la suite documentaire :
   - `README.md`
   - `VISION.md`
   - `ARCHITECTURE.md`
   - `REQUIREMENTS.md`
   - `ROADMAP.md`
   - `DECISIONS.md` (ADR-001 Modular Monolith, ADR-002 Decoupled RBAC, ADR-003 Geo Abstraction, ADR-004 Reputation Engine)
   - `DEVELOPMENT_GUIDE.md`
   - `PROJECT_STATUS.md`
   - `CHANGELOG.md`
   - `docs/daily/2026-08-15-inception.md`
3. Structure de dossiers initiale du backend et du frontend.

---

## 9. Risks (Analyse des Risques & Mitigations)

| Risque Identifié | Impact | Stratégie de Mitigation |
| :--- | :--- | :--- |
| **Fuite de coordonnées géographiques précises (vie privée)** | Élevé | Masquage par défaut au niveau du domaine et des DTOs (seules la ville/zone approximative sont publiques, coordonnées précises réservées aux besoins opérationnels stricts). |
| **Confusion entre Rôles et Pouvoirs absolus (ex: Imam)** | Moyen | Modèle RBAC granulaire basé sur les permissions réelles (`CAN_VERIFY_MOSQUE`, `CAN_APPROVE_INITIATIVE`, `CAN_MANAGE_DONATIONS`), vérifiables et auditables. |
| **Complexité géospatiale excessive précoce** | Moyen | Abstraction `GeoLocation` avec calcul de distance Haversine en Java/SQL standard pour la v1, avec transition transparente vers PostGIS sans impacter la couche métier. |
| **Over-engineering des fonctionnalités non-MVP** | Moyen | Respect strict des gates de validation à chaque milestone. |
