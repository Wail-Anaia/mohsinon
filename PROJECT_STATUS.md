# ÉTAT D'AVANCEMENT DU PROJET (PROJECT_STATUS.md) — MOHSINON

**Dernière mise à jour :** 30 Août 2026  
**Statut Global :** **Milestone 3 Terminé, Validé & Clôturé** 🚀  

---

## 📊 Tableau de Bord des Milestones

| Milestone | Périmètre | Statut | Tests / Validation |
| :--- | :--- | :---: | :--- |
| **Milestone 0** | **Foundation + Documentation + Repository** | 🟢 **Terminé** | Documentation rédigée, Git initialisé, `.gitignore` créé, arborescences créées. |
| **Milestone 1** | **Core + Config + Database + BaseEntity + Exceptions** | 🟢 **Terminé** | 24 tests unitaires & d'intégration passés (GeoLocation, BaseEntity UUID, PageResponse, Impact Ledger, RFC 7807). |
| **Milestone 2** | **Identity + Authentication + JWT + Refresh Tokens** | 🟢 **Terminé** | 41 tests passés (Enregistrement, Login, JWT Access/Refresh, Rotation automatique, Détection de réutilisation/fraude, Logout, Migration Flyway V1). |
| **Milestone 3** | **Authorization + Roles + Permissions + Contextual Memberships** | 🟢 **Terminé & Clôturé** | **110 tests passés** (Pipeline Deny by Default, PermissionRegistry in-memory, Membership lifecycle, Isolation stricte cross-mosque, Évaluateur SpEL `@authz`, Migration Flyway V2 sur PostgreSQL 18.2). |
| **Milestone 4** | **Users + Profiles** | ⚪ À venir | Prochaine étape prioritaire. |
| **Milestone 5** | **Mosques + Imam + Mosque Committee + Memberships** | ⚪ À venir | - |
| **Milestone 6** | **Donations Multi-Ressources** | ⚪ À venir | - |
| **Milestone 7** | **Volunteers + Skills** | ⚪ À venir | - |
| **Milestone 8** | **Initiatives + Projects** | ⚪ À venir | - |
| **Milestone 9** | **Impact + Reputation + Badges** | ⚪ À venir | - |

---

## 🔍 Détail du Milestone 3 (Authorization & Contextual Governance)

### Composants Implémentés & Validés (M3.1 à M3.9) :
1. ✅ **Enums & Modèle de Domaine** :
   - `GlobalRoleType` (`ROLE_USER`, `ROLE_VOLUNTEER`, `ROLE_DONOR`, `ROLE_ADMIN`).
   - `MembershipRole` (`IMAM`, `MOSQUE_PRESIDENT`, `MOSQUE_COMMITTEE_MEMBER`, `TREASURER`, `VOLUNTEER_COORDINATOR`, `DONATION_MANAGER`, `LOCAL_MODERATOR`).
   - `MembershipStatus` (`ACTIVE`, `PENDING_APPROVAL`, `REVOKED`, `EXPIRED`).
   - `PermissionType` (Capacités métier type-safe).
   - `ResourceContext` (Value Object immuable encapsulant `resourceType` et `resourceId`).
2. ✅ **Registre Statique des Permissions (`PermissionRegistry`)** :
   - Mappings immuables in-memory Rôles Globaux $\rightarrow$ Permissions et Rôles de Membership $\rightarrow$ Permissions.
   - Résolution à coût zéro jointure SQL (ADR-013).
3. ✅ **Modèle de Persistance & Repositories** :
   - `GlobalRole`, `UserGlobalRole`, `Membership` (dérivant de `BaseEntity`).
   - `GlobalRoleRepository`, `UserGlobalRoleRepository`, `MembershipRepository` (avec méthode optimisée `findEffectiveMemberships`).
4. ✅ **Moteur d'Autorisation (`AuthorizationService` / `DefaultAuthorizationService`)** :
   - Pipeline d'évaluation Deny-by-Default en 6 étapes (ADR-012).
   - Assertions typées `requireGlobalPermission`, `requirePermission`.
   - Méthodes métier spécialisées `canManageMosque`, `isAdmin`.
5. ✅ **Service de Gestion des Appartenances (`MembershipService`)** :
   - Affectation, réactivation, expiration temporelle et révocation de positions locales.
6. ✅ **Intégration Spring Security SpEL (`SecurityAuthzEvaluator` / `@authz`)** :
   - Évaluateur déclaratif mince pour annotations `@PreAuthorize`.
   - Liaison dynamique des autorités globales dans `UserPrincipal` et `JwtAuthenticationFilter`.
7. ✅ **Migration Base de Données (Flyway V2)** :
   - Script `V2__init_authorization_schema.sql` appliqué et validé sur PostgreSQL 18.2 (`mohsinon_db`) avec `ddl-auto: validate`.
8. ✅ **Tests de Sécurité & Preuves d'Isolation** :
   - 110 tests exécutés avec succès (`mvn clean test`), incluant la preuve formelle d'isolation inter-mosquées, l'étanchéité inter-ressources et la frontière d'authentification 401/403.

---

## 🎯 Prochaine Étape Immédiate
👉 **Milestone 4 : Users + Profiles**
- Profils utilisateurs détaillés (bio, avatar, préférences, compétences déclarées).
- Gestion de profil utilisateur (mise à jour d'informations, changement de mot de passe).
- Protection des routes profil via le moteur d'autorisation du Milestone 3.
