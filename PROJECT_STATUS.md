# ÉTAT D'AVANCEMENT DU PROJET (PROJECT_STATUS.md) — MOHSINON

**Dernière mise à jour :** 15 Août 2026  
**Statut Global :** En cours — **Milestone 0 : Foundation, Documentation & Repository Skeleton**  

---

## 📊 Tableau de Bord des Milestones

| Milestone | Périmètre | Statut | Tests / Validation |
| :--- | :--- | :---: | :--- |
| **Milestone 0** | **Foundation + Documentation + Repository** | 🟢 **Terminé** | Documentation rédigée, Git initialisé, `.gitignore` créé, arborescences créées. |
| **Milestone 1** | **Core + Config + Database + BaseEntity + Exceptions** | ⚪ À venir | Prochaine étape prioritaire. |
| **Milestone 2** | **Identity + Authentication + JWT + Refresh Tokens** | ⚪ À venir | - |
| **Milestone 3** | **Authorization + Roles + Permissions** | ⚪ À venir | - |
| **Milestone 4** | **Users + Profiles** | ⚪ À venir | - |
| **Milestone 5** | **Mosques + Imam + Mosque Committee + Memberships** | ⚪ À venir | - |
| **Milestone 6** | **Donations Multi-Ressources** | ⚪ À venir | - |
| **Milestone 7** | **Volunteers + Skills** | ⚪ À venir | - |
| **Milestone 8** | **Initiatives + Projects** | ⚪ À venir | - |
| **Milestone 9** | **Impact + Reputation + Badges** | ⚪ À venir | - |

---

## 🔍 Détail du Milestone Actuel (Milestone 0)

### Actions Réalisées :
1. ✅ **Initialisation Git** : Dépôt initialisé sur la branche `main` avec remote configuré vers `https://github.com/Wail-Anaia/mohsinon.git`.
2. ✅ **Création du `.gitignore`** universel pour Java/Spring, Node/Angular, IDEs et systèmes d'exploitation.
3. ✅ **Rédaction du Référentiel Documentaire** :
   - `README.md`
   - `VISION.md`
   - `ARCHITECTURE.md`
   - `REQUIREMENTS.md`
   - `ROADMAP.md`
   - `DECISIONS.md` (ADR-001 à ADR-007)
   - `DEVELOPMENT_GUIDE.md`
   - `PROJECT_STATUS.md`
   - `CHANGELOG.md`
   - `IMPLEMENTATION_BASELINE.md`
4. ✅ **Création des Dossiers Thématiques** : `docs/architecture/`, `docs/api/`, `docs/database/`, `docs/security/`, `docs/decisions/`, `docs/daily/`.
5. ✅ **Journal de bord initial** : `docs/daily/2026-08-15-milestone-0-foundation.md`.

---

## 🎯 Prochaine Étape Immédiate
👉 **Milestone 1 : Core + Configuration + Database + BaseEntity + Exception Handling**
- Initialisation du projet backend Maven Spring Boot 3 / Java 17.
- Mise en place du module `com.mohsinon.core` :
  - `BaseEntity` (ID, audit `createdAt`, `updatedAt`, `version`).
  - RFC 7807 `ProblemDetail` & `GlobalExceptionHandler`.
  - Abstraction géographique `GeoLocation` (Haversine v1).
  - Profils `dev` (H2 PostgreSQL) et `prod` (PostgreSQL).
  - Tests unitaires du Core.
