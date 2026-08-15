# ÉTAT D'AVANCEMENT DU PROJET (PROJECT_STATUS.md) — MOHSINON

**Dernière mise à jour :** 15 Août 2026  
**Statut Global :** **Milestone 2 Terminé & Validé** 🚀  

---

## 📊 Tableau de Bord des Milestones

| Milestone | Périmètre | Statut | Tests / Validation |
| :--- | :--- | :---: | :--- |
| **Milestone 0** | **Foundation + Documentation + Repository** | 🟢 **Terminé** | Documentation rédigée, Git initialisé, `.gitignore` créé, arborescences créées. |
| **Milestone 1** | **Core + Config + Database + BaseEntity + Exceptions** | 🟢 **Terminé** | 24 tests unitaires & d'intégration passés (GeoLocation, BaseEntity UUID, PageResponse, Impact Ledger, RFC 7807). |
| **Milestone 2** | **Identity + Authentication + JWT + Refresh Tokens** | 🟢 **Terminé** | **41 tests passés** (Enregistrement, Login, JWT Access/Refresh, Rotation automatique, Détection de réutilisation/fraude, Logout, Migration Flyway V1). |
| **Milestone 3** | **Authorization + Roles + Permissions** | ⚪ À venir | Prochaine étape prioritaire. |
| **Milestone 4** | **Users + Profiles** | ⚪ À venir | - |
| **Milestone 5** | **Mosques + Imam + Mosque Committee + Memberships** | ⚪ À venir | - |
| **Milestone 6** | **Donations Multi-Ressources** | ⚪ À venir | - |
| **Milestone 7** | **Volunteers + Skills** | ⚪ À venir | - |
| **Milestone 8** | **Initiatives + Projects** | ⚪ À venir | - |
| **Milestone 9** | **Impact + Reputation + Badges** | ⚪ À venir | - |

---

## 🔍 Détail du Milestone 2 (Identity & Authentication)

### Composants Implémentés :
1. ✅ **Entités & Modèle de Domaine** :
   - `User` (`UUID id`, `username` unique normalisé, `email` unique normalisé, `passwordHash` BCrypt, `firstName`, `lastName`, `displayName`, `status`).
   - `UserStatus` (`ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`).
   - `RefreshToken` (Stockage sécurisé par empreinte SHA-256, `expiresAt`, `revokedAt`, `replacedByTokenHash`, `ipAddress`, `userAgent`).
2. ✅ **Sécurité & Fournisseur de Jetons** :
   - `TokenProvider` (JJWT 0.12, signature HMAC-SHA-256, Access Token 15 min, Refresh Token 7 jours, hachage SHA-256).
   - `JwtAuthenticationFilter` (Extraction Bearer token, validation et injection `UserPrincipal` dans le SecurityContext).
   - `JwtAuthenticationEntryPoint` (Formatage standardisé des erreurs 401 Unauthorized en RFC 7807 ProblemDetail).
   - `CurrentUserProvider` (Abstraction `SecurityContextCurrentUserProvider` découplant les domaines applicatifs).
3. ✅ **Service d'Authentification (`AuthService`)** :
   - Inscription (`register`) avec vérification des conflits d'email et username.
   - Connexion (`login`) avec rejet sécurisé des identifiants invalides ou comptes suspendus.
   - Renouvellement avec rotation atomique (`refreshToken`) et détection de réutilisation/vol de jeton avec révocation globale immédiate.
   - Déconnexion (`logout`) avec révocation des jetons actifs.
4. ✅ **Contrôleur REST (`/api/v1/auth/*`)** :
   - `POST /api/v1/auth/register` (201 Created)
   - `POST /api/v1/auth/login` (200 OK)
   - `POST /api/v1/auth/refresh` (200 OK)
   - `POST /api/v1/auth/logout` (200 OK)
   - `GET /api/v1/auth/me` (200 OK - Authentification Bearer requise)
5. ✅ **Base de Données & Migrations** :
   - Script Flyway `V1__init_identity_schema.sql` (`users`, `refresh_tokens`, `impact_transactions` avec index et contraintes d'intégrité).
6. ✅ **Tests Automatisés** : 41 tests unitaires et d'intégration validés sans erreur (`mvn clean test`).

---

## 🎯 Prochaine Étape Immédiate
👉 **Milestone 3 : Authorization + Roles + Permissions**
- Entités `Role` et `Permission` découplées.
- Rôles initiaux : `ROLE_USER`, `ROLE_VOLUNTEER`, `ROLE_DONOR`, `ROLE_IMAM`, `ROLE_MOSQUE_COMMITTEE`, `ROLE_ADMIN`.
- Permissions granulaires (`MOSQUE_CREATE`, `MOSQUE_VERIFY`, `DONATION_MANAGE`, etc.).
- Annotation de sécurité `@RequirePermission` et liaison avec `UserPrincipal`.
- Tests d'autorisation et d'accès contextuel.
