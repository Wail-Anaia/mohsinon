# Mohsinon — Plateforme Mondiale d'Entraide et d'Impact

> **« Une idée ne doit pas rester une idée lorsqu'une communauté peut la transformer en action. »**

Mohsinon est une infrastructure numérique mondiale conçue pour transformer l'entraide, le bénévolat, le partage de compétences, les dons multi-ressources, les idées et les initiatives locales en **impact réel et mesurable dans le monde physique**.

La première phase du projet s'articule autour de la **Mosquée comme centre d'organisation communautaire local**, administrée par son Imam et son Comité de mosquée, avant d'évoluer vers un écosystème mondial de coopération et de compétences.

---

## 🌟 Vision & Philosophie

Mohsinon n'est pas un simple réseau social axé sur la visibilité ou les « likes ».  
C'est une **plateforme d'action et d'impact** :
- **Action plutôt que visibilité** : La réputation repose sur des contributions vérifiées, des projets réalisés et de l'aide concrète apportée aux personnes et communautés.
- **Entraide multi-ressources** : Dépasser le simple don financier pour inclure l'argent, les vêtements, les vivres, les meubles, les livres, le matériel, le temps et les compétences.
- **Proximité & Confidentialité** : Permettre aux membres de découvrir les besoins et opportunités locales tout en garantissant la confidentialité absolue de leur localisation exacte.
- **Gouvernance équilibrée** : Séparation stricte des Rôles et des Permissions au sein de chaque communauté.

---

## 🏗️ Architecture Globale

Mohsinon est conçu selon le paradigme **Modular Monolith** avec une approche **Core-First** et les principes du **Domain-Driven Design (DDD)** :

```text
Mohsinon
│
├── Mohsinon Core (Security, Identity, Audit, Geo Abstraction, Common Kernel)
│
├── Phase MVP (Local Hub & Mutual Aid)
│   ├── Mosques (Hubs locaux, Imams, Comités de mosquée, Profils)
│   ├── Donations (Dons multi-ressources : argent, vivres, biens, temps)
│   ├── Volunteers & Skills (Répertoire des compétences, matching besoins)
│   ├── Initiatives & Projects (Besoins, idées, jalons, exécution, preuves)
│   └── Reputation & Impact (Points d'impact, contributions vérifiées, badges)
│
└── Extensions Futures (Post-MVP)
    ├── Education & Mentoring
    ├── Marketplace d'Échange
    ├── Jobs & Insertion
    ├── Gouvernance Décentralisée
    └── Mohsinon AI
```

---

## 🛠️ Stack Technique

- **Backend** : Java 17 LTS, Spring Boot 3.x, Spring Security (JWT Stateless + Refresh Tokens), Spring Data JPA, Hibernate.
- **Base de Données** : PostgreSQL (avec support PostGIS pour la géolocalisation) / H2 en mémoire pour le développement sans friction.
- **Migrations** : Flyway.
- **API Documentation** : OpenAPI 3 / Swagger.
- **Frontend** : Angular 18+ (Standalone Components, Signals, SCSS, Design System sur-mesure accessible).
- **DevOps & Conteneurs** : Docker, Docker Compose, GitHub Actions CI/CD.

---

## 📚 Référentiel Documentaire

Le projet maintient une documentation vivante servant de source de vérité unique :

- [`VISION.md`](./VISION.md) : Manifeste, philosophie et mission à long terme.
- [`ARCHITECTURE.md`](./ARCHITECTURE.md) : Conception logicielle, DDD, frontières modulaires et flux de données.
- [`REQUIREMENTS.md`](./REQUIREMENTS.md) : Exigences fonctionnelles, non-fonctionnelles, sécurité et RGPD.
- [`ROADMAP.md`](./ROADMAP.md) : Jalons de développement séquentiels (Milestones 0 à 9).
- [`DECISIONS.md`](./DECISIONS.md) : Architecture Decision Records (ADRs).
- [`DEVELOPMENT_GUIDE.md`](./DEVELOPMENT_GUIDE.md) : Guide de mise en route, normes de code et tests.
- [`PROJECT_STATUS.md`](./PROJECT_STATUS.md) : Tableau de bord de l'avancement en temps réel.
- [`IMPLEMENTATION_BASELINE.md`](./IMPLEMENTATION_BASELINE.md) : Baseline technique initiale.
- [`CHANGELOG.md`](./CHANGELOG.md) : Historique des versions et changements.
- [`docs/`](./docs/) : Dossiers thématiques (`architecture/`, `api/`, `database/`, `security/`, `daily/`).

---

## 🚀 Démarrage Rapide

### Prérequis
- Java 17 LTS (JDK 17+)
- Node.js v18+ (recommandé v24) & NPM
- Git

### Lancement du Backend
```powershell
cd backend
./mvnw.cmd spring-boot:run
```
L'API REST est accessible sur `http://localhost:8080` et la documentation Swagger sur `http://localhost:8080/swagger-ui.html`.

### Lancement du Frontend
```powershell
cd frontend
npm install
npm start
```
L'application web est accessible sur `http://localhost:4200`.

---

## 📜 Licence & Éthique
Mohsinon est développé dans un esprit de service communautaire, de transparence et d'excellence technique.
