# Journal de Bord — 15 Août 2026 (Milestone 2)

**Objet :** Exécution et Validation du Milestone 2 (Identity & Authentication)  
**Auteur :** Équipe d'Ingénierie Mohsinon  

---

## 1. Contexte & Objectifs

Le Milestone 2 met en place le socle d'identité et d'authentification de Mohsinon. Il s'agit d'une fondation critique devant garantir la sécurité des accès, la confidentialité des données, l'unicité des comptes et la résistance aux attaques par rejeu de jetons.

---

## 2. Réalisations Techniques

1. **Modèle de Domaine Identity** :
   - `User` avec `UUID id`, `username`, `email`, `passwordHash`, `firstName`, `lastName`, `displayName`, et `status`.
   - `UserStatus` (`ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`).
   - `RefreshToken` persisté avec hachage **SHA-256** (zéro secret brut en base) et liaison de remplacement (`replacedByTokenHash`).
2. **Fournisseur de Jetons & Sécurité** :
   - `TokenProvider` : Access Tokens signés HMAC-SHA-256 avec claims minimaux (15 min) et Refresh Tokens cryptographiques (7 jours).
   - `JwtAuthenticationFilter` : Interception, validation et injection du `UserPrincipal` dans le `SecurityContext`.
   - `JwtAuthenticationEntryPoint` : Traduction des accès 401 non authentifiés en **Problem Details RFC 7807**.
   - `CurrentUserProvider` : Abstraction d'accès à l'utilisateur courant, protégeant les couches applicatives du couplage avec `SecurityContextHolder`.
3. **Service & Logique Métier d'Authentification** :
   - Inscription avec vérification des conflits d'unicité et hachage BCrypt.
   - Authentification par identifiant (email ou nom d'utilisateur) avec messages vagues anti-énumération.
   - Rotation automatique des Refresh Tokens : l'ancien token est révoqué et remplacé par le nouveau.
   - **Détection proactive de réutilisation / vol de jeton** : Si un token déjà révoqué/remplacé est réutilisé, toutes les sessions actives de l'utilisateur sont instantanément révoquées.
4. **API REST `/api/v1/auth/*`** :
   - `POST /api/v1/auth/register` (201 Created)
   - `POST /api/v1/auth/login` (200 OK)
   - `POST /api/v1/auth/refresh` (200 OK)
   - `POST /api/v1/auth/logout` (200 OK)
   - `GET /api/v1/auth/me` (200 OK avec Bearer auth)
5. **Schéma Relationnel & Flyway** :
   - Script `V1__init_identity_schema.sql` configuré.
6. **Architecture Decision Records** :
   - **ADR-010** : Stockage Haché (SHA-256) & Rotation Automatique des Refresh Tokens avec Détection de Fraude.
   - **ADR-011** : Abstraction `CurrentUserProvider` pour le Découplage de la Sécurité.

---

## 3. Bilan des Tests

```text
[INFO] Results:
[INFO] Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
- Tests unitaires et d'intégration validés à 100% sans avertissements.

---

## 4. Prochaine Étape

- Validation par l'utilisateur du Milestone 2.
- Lancement du **Milestone 3 : Authorization + Roles + Permissions**.
