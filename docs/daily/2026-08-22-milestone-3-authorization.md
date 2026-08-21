# Journal de Bord — 22 Août 2026 (Milestone 3)

**Objet :** Implémentation et Validation du Moteur d'Autorisation Tridimensionnel & Gouvernance Locale (Milestone 3)  
**Auteur :** Équipe d'Ingénierie Mohsinon  

---

## 1. Contexte & Objectifs

Le Milestone 3 dote Mohsinon d'un moteur d'autorisation d'entreprise robuste, conçu pour la gouvernance communautaire locale autonome :
$$\text{Rôle Global (Identité)} \times \text{Membership (Position Locale)} \times \text{Permission Granulaire (Action)} \times \text{ResourceContext (Lieu)}$$

L'exigence critique non négociable était de **prouver formellement l'isolation inter-mosquées** (l'Imam de la Mosquée A ne peut en aucun cas administrer la Mosquée B).

---

## 2. Réalisations Techniques

1. **Modèle de Domaine & Types Forts (`com.mohsinon.core.security.authorization.model`)** :
   - `GlobalRoleType` : `ROLE_USER`, `ROLE_VOLUNTEER`, `ROLE_DONOR`, `ROLE_ADMIN`.
   - `MembershipRole` : `IMAM`, `MOSQUE_PRESIDENT`, `MOSQUE_COMMITTEE_MEMBER`, `TREASURER`, `VOLUNTEER_COORDINATOR`, `DONATION_MANAGER`, `LOCAL_MODERATOR`.
   - `MembershipStatus` : `ACTIVE`, `PENDING_APPROVAL`, `REVOKED`, `EXPIRED`.
   - `PermissionType` : Référentiel type-safe des capacités métier.
   - `ResourceContext` : Value Object immuable `(resourceType, resourceId)` avec constructeurs typés (`ResourceContext.mosque(id)`).
2. **Registre Statique des Permissions (`PermissionRegistry`)** :
   - Mappings immuables in-memory associant rôles globaux et positions locales aux ensembles de permissions.
   - Résolution à zéro jointure SQL et performance optimale.
3. **Persistance & Entités JPA (`com.mohsinon.core.security.authorization.entity`)** :
   - `GlobalRole` : Rôles globaux de la plateforme.
   - `UserGlobalRole` : Attribution N:N persistée des rôles globaux aux utilisateurs.
   - `Membership` : Appartenances locales dérivant de `BaseEntity` (avec gestion de statut, audit `assignedBy` et date d'expiration optionnelle).
   - Repositories optimisés `GlobalRoleRepository`, `UserGlobalRoleRepository`, et `MembershipRepository` (avec requête indexée `findEffectiveMemberships`).
4. **Moteur d'Autorisation Central (`AuthorizationService` / `DefaultAuthorizationService`)** :
   - Pipeline d'évaluation **Deny by Default** en 6 étapes :
     1. Vérification du statut actif de l'utilisateur.
     2. Bypass `ROLE_ADMIN` avec audit.
     3. Vérification des permissions globales.
     4. Vérification de la présence du `ResourceContext`.
     5. Recherche des memberships actifs non expirés et validation de la position locale.
     6. Rejet par défaut (403 Forbidden).
5. **Gestionnaire d'Appartenances (`MembershipService`)** :
   - Affectation, réactivation, révocation et consultation des memberships actifs par utilisateur et par ressource.
6. **Évaluateur Spring Security SpEL (`SecurityAuthzEvaluator` / `@authz`)** :
   - Composant `@Component("authz")` utilisable de façon concise dans les annotations `@PreAuthorize` sans logique métier dans l'expression.
7. **Base de Données & Migrations** :
   - Script Flyway `V2__init_authorization_schema.sql` (`global_roles`, `user_global_roles`, `memberships`, index et seed des rôles).
8. **Architecture Decision Records** :
   - **ADR-012** : Modèle d'Autorisation Hybride Tridimensionnel & Gouvernance Locale Autonome.
   - **ADR-013** : Résolution Statique en Mémoire des Permissions et Intégration Déclarative SpEL (`@authz`).

---

## 3. Bilan des Tests Automatisés

```text
[INFO] Results:
[INFO] Tests run: 66, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Preuves Formelles de Sécurité & Isolation :
- **Imam A sur Mosquée A** $\rightarrow$ **200 OK** (Autorisé).
- **Imam A sur Mosquée B** $\rightarrow$ **403 Forbidden** (Isolation inter-mosquées validée).
- **Imam B sur Mosquée B** $\rightarrow$ **200 OK** (Autorisé).
- **Imam B sur Mosquée A** $\rightarrow$ **403 Forbidden** (Isolation inter-mosquées validée).
- **Trésorier A sur Mosquée A** $\rightarrow$ **403 Forbidden** pour la mise à jour de la mosquée (Capacité non accordée).
- **Utilisateur non authentifié** $\rightarrow$ **401 Unauthorized**.
- **Utilisateur suspendu** $\rightarrow$ **401/403 Denied**.
- **Membership révoqué ou expiré** $\rightarrow$ **403 Forbidden**.
- **Super-Admin** $\rightarrow$ **200 OK** sur toutes les mosquées.

---

## 4. Prochaine Étape

- Lancement du **Milestone 4 : Users + Profiles**.
