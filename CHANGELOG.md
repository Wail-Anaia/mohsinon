# JOURNAL DES MODIFICATIONS (CHANGELOG) — MOHSINON

Toutes les modifications notables apportées au projet Mohsinon sont consignées dans ce document.  
Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.0.0/), et ce projet adhère à [Semantic Versioning](https://semver.org/lang/fr/).

---

## [0.2.0-alpha] - 2026-08-15

### Ajouté (Milestone 1 : Core Foundation & Infrastructure)
- **BaseEntity** : Entité de base standardisée avec identifiant `UUID`, audit timestamps (`createdAt`, `updatedAt`) et optimistic locking `@Version`.
- **GeoLocation** : Value Object `@Embeddable` avec calcul géodésique Haversine (`distanceToInKm`) et méthode de floutage de coordonnées pour le respect de la vie privée (`toApproximate()`).
- **RFC 7807 Problem Details** : `GlobalExceptionHandler` unifié et hiérarchie d'exceptions (`BusinessException`, `ResourceNotFoundException`, `ConflictException`, `ForbiddenException`, `ValidationException`).
- **Pagination & Sorting** : DTOs génériques indépendants du domaine (`PageResponse<T>`, `PaginationRequest`, `SortDirection`).
- **Impact Transaction Ledger** : Modèle immuable de transactions d'impact (`ImpactTransaction`, `ImpactTransactionType`) et repository avec calcul de solde agrégé.
- **Sécurité Core & Configuration** : `CoreSecurityConfig`, `JpaConfig`, `OpenApiConfig`, contrôleur de santé `/api/v1/health`.
- **Profils Multi-Environnements** : `application-dev.yml` (H2 compatible PostgreSQL), `application-prod.yml` (PostgreSQL), `application-test.yml`.
- **Architecture Decisions** : Ajout de l'ADR-008 (UUID Universel) et de l'ADR-009 (Impact Ledger).
- **Tests** : 24 tests unitaires et d'intégration validés sans erreur.

---

## [0.1.0-alpha] - 2026-08-15

### Ajouté (Milestone 0 : Foundation, Documentation & Repository)
- Initialisation du dépôt Git avec configuration de la branche `main` et remote origin.
- `.gitignore` universel pour Java, Maven, Angular, Node, IDEs.
- Suite documentaire complète (`README.md`, `VISION.md`, `ARCHITECTURE.md`, `REQUIREMENTS.md`, `ROADMAP.md`, `DECISIONS.md`, `DEVELOPMENT_GUIDE.md`, `PROJECT_STATUS.md`, `CHANGELOG.md`, `IMPLEMENTATION_BASELINE.md`).
- Arborescence `docs/` (`architecture/`, `api/`, `database/`, `security/`, `decisions/`, `daily/`).
