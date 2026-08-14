# GUIDE DE DÉVELOPPEMENT (DEVELOPMENT_GUIDE.md) — MOHSINON

Ce guide rassemble toutes les instructions nécessaires pour installer, exécuter, tester et contribuer au projet Mohsinon selon les standards d'ingénierie définis.

---

## 1. Prérequis & Environnement Local

- **Java JDK** : Version 17 LTS (Java SE 17+).
- **Node.js** : Version 18.x LTS minimum (v24.x supportée).
- **NPM** : Version 9.x+ (v11.x supportée).
- **Git** : Version 2.40+.
- **IDE recommandé** : VS Code / IntelliJ IDEA / Eclipse.

---

## 2. Structure du Dépôt

```text
mohsinon/
├── backend/                  # Application Spring Boot 3 / Java 17
│   ├── src/main/java/        # Code source modulaire
│   ├── src/main/resources/   # Configuration et scripts de migration Flyway
│   ├── src/test/java/        # Tests unitaires et d'intégration
│   ├── pom.xml               # Configuration Maven
│   └── mvnw / mvnw.cmd       # Wrapper Maven
│
├── frontend/                 # Application Web Angular Standalone
│   ├── src/app/              # Core, Shared, Layouts, Features
│   ├── src/styles/           # Design System SCSS et variables HSL
│   └── package.json          # Dépendances et scripts NPM
│
├── docs/                     # Documentation thématique et journaux quotidiens
├── docker-compose.yml        # Orchestration PostgreSQL/PostGIS et services
└── .gitignore                # Règles d'exclusion Git universelles
```

---

## 3. Commandes & Exécution

### 3.1 Backend (Spring Boot)

Le backend utilise le Maven Wrapper pour garantir une exécution reproductible sans nécessiter d'installation Maven globale :

```powershell
# Accéder au dossier backend
cd backend

# Compiler le projet
./mvnw.cmd clean compile

# Lancer la suite de tests
./mvnw.cmd test

# Démarrer le serveur en mode développement (profil dev avec H2 PostgreSQL)
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Packager l'application en JAR exécutable
./mvnw.cmd clean package -DskipTests
```

- **URL de l'API** : `http://localhost:8080`
- **Swagger UI / OpenAPI** : `http://localhost:8080/swagger-ui.html`
- **Console H2 (mode dev)** : `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:mohsinon_dev;MODE=PostgreSQL`)

---

### 3.2 Frontend (Angular)

```powershell
# Accéder au dossier frontend
cd frontend

# Installer les dépendances
npm install

# Démarrer le serveur de développement Angular
npm start

# Lancer les tests unitaires
npm test

# Compiler pour la production
npm run build
```

- **URL de l'application Web** : `http://localhost:4200`

---

## 4. Conventions de Code & Qualité

### 4.1 Principes Fondamentaux
- **Solidité & Simplicité** : Appliquer strictement les principes SOLID, KISS et DRY.
- **Pas de God Classes** : Chaque service et composant a une responsabilité unique et délimitée.
- **DTOs Dédiés** : Aucune entité JPA n'est exposée directement dans les contrôleurs REST. Toujours utiliser des DTOs validés (`@Valid`, Bean Validation).
- **Gestion des Exceptions** : Lever des exceptions métier dérivées de `BusinessException` et laisser le `GlobalExceptionHandler` formater la réponse RFC 7807 `ProblemDetail`.

### 4.2 Conventions Git & Commits
Nous suivons la convention **Conventional Commits** :

```text
<type>(<scope>): <description courte>

[corps optionnel décrivant le pourquoi et les détails]
```

Types autorisés :
- `feat` : Nouvelle fonctionnalité
- `fix` : Correction de bug
- `docs` : Modification de documentation
- `refactor` : Refactorisation sans changement de comportement
- `test` : Ajout ou modification de tests
- `chore` : Tâches de maintenance, configuration build, dépendances

Exemples :
- `feat(auth): implement JWT authentication filter and refresh token rotation`
- `docs(architecture): add ADR-005 on geolocation strategy`
- `test(mosques): add integration tests for nearby mosque query`

---

## 5. Stratégie de Branches

- `main` : Branche principale stable et déployable en continu.
- Pour les évolutions : développement par milestones validés avec des commits propres et descriptifs.
