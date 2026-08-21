# REGISTRE DES DÉCISIONS D'ARCHITECTURE (DECISIONS.md)

Ce document enregistre les décisions d'architecture majeures (ADRs - Architecture Decision Records) prises pour le projet Mohsinon, leur contexte, leurs justifications et leurs conséquences.

---

## Sommaire des ADRs

| ID | Titre | Statut | Date |
| :--- | :--- | :--- | :--- |
| **ADR-001** | Choix du modèle Modular Monolith plutôt que Microservices | **Accepté** | 2026-08-15 |
| **ADR-002** | Approche Core-First et Inversion de Dépendances | **Accepté** | 2026-08-15 |
| **ADR-003** | Modèle d'Autorisation Découplé (Rôles vs Permissions vs Contexte) | **Accepté** | 2026-08-15 |
| **ADR-004** | Modèle de Don Multi-Ressources Extensible | **Accepté** | 2026-08-15 |
| **ADR-005** | Abstraction Géographique Haversine vers PostGIS | **Accepté** | 2026-08-15 |
| **ADR-006** | Moteur de Réputation Basé sur des Preuves Réelles (Non-Crypto) | **Accepté** | 2026-08-15 |
| **ADR-007** | Frontend Angular Standalone avec Signals et Design System Dédié | **Accepté** | 2026-08-15 |
| **ADR-008** | UUID comme Identifiant Standard Universel des Entités | **Accepté** | 2026-08-15 |
| **ADR-009** | Architecture en Ledger Immuable pour les Points d'Impact (`ImpactTransaction`) | **Accepté** | 2026-08-15 |
| **ADR-010** | Stockage Haché (SHA-256) & Rotation Automatique des Refresh Tokens avec Détection de Fraude | **Accepté** | 2026-08-15 |
| **ADR-011** | Abstraction `CurrentUserProvider` pour le Découplage de la Sécurité | **Accepté** | 2026-08-15 |
| **ADR-012** | Modèle d'Autorisation Hybride Tridimensionnel & Gouvernance Locale Autonome | **Accepté** | 2026-08-22 |
| **ADR-013** | Résolution Statique en Mémoire des Permissions et Intégration Déclarative SpEL (`@authz`) | **Accepté** | 2026-08-22 |

---

### ADR-001 : Choix du modèle Modular Monolith plutôt que Microservices
**Contexte :** Mohsinon a vocation à devenir une infrastructure mondiale. Le réflexe courant est de démarrer directement en microservices distribués.  
**Décision :** Nous adoptons un **Modular Monolith** avec Spring Boot 3.x et Java 17. Le code est partitionné en modules métier étanches (`com.mohsinon.modules.*`), communiquant via des interfaces publiques et des événements de domaine.  
**Conséquences :** Zéro surcoût d'orchestration distribuée, cohérence transactionnelle ACID native, déploiement simple sur serveur unique, et possibilité d'extraire des microservices plus tard sans friction.

---

### ADR-002 : Approche Core-First et Inversion de Dépendances
**Contexte :** Chaque module métier a besoin de sécurité, d'audit, de pagination, de gestion d'erreurs et d'entités communes.  
**Décision :** Nous développons d'abord le noyau commun `com.mohsinon.core`. Les modules métier dépendent du Core, mais le Core n'a aucune dépendance envers les modules métier.  
**Conséquences :** Réutilisabilité maximale, cohérence de l'API REST (format RFC 7807 uniforme), stabilité architecturale garantie.

---

### ADR-003 : Modèle d'Autorisation Découplé (Rôles vs Permissions vs Contexte)
**Contexte :** Dans une communauté, un utilisateur peut être un simple membre globalement, mais Imam ou responsable financier dans une mosquée précise.  
**Décision :** Découplage strict entre Rôles Globaux, Permissions Granulaires et Appartenances Contextuelles (`MosqueMember`).  
**Conséquences :** Évolutivité totale, sécurité granulaire, respect du principe de moindre privilège.

---

### ADR-004 : Modèle de Don Multi-Ressources Extensible
**Contexte :** Les plateformes caritatives se limitent souvent à la collecte de fonds. Mohsinon encourage le don de vivres, meubles, temps, compétences, outils et livres.  
**Décision :** Entité `DonationItem` générique et polymorphique via une catégorie (`DonationCategory`) et attributs souples.  
**Conséquences :** Ajout immédiat de nouveaux types de dons sans migration lourde.

---

### ADR-005 : Abstraction Géographique Haversine vers PostGIS
**Contexte :** La recherche de proximité est essentielle, mais imposer PostGIS dès le premier jour local complique le setup.  
**Décision :** Abstraction `GeoLocation` Value Object avec formule Haversine en v1 (H2/PostgreSQL), prête pour la transition vers les index spatiaux PostGIS natifs (`ST_DWithin`) en production.  
**Conséquences :** Démarrage local immédiat à zéro friction et performance garantie à l'échelle.

---

### ADR-006 : Moteur de Réputation Basé sur des Preuves Réelles (Non-Crypto)
**Contexte :** L'impact communautaire doit être mesuré sans tomber dans la spéculation ou la blockchain superflue.  
**Décision :** Réputation fondée sur des `Impact Points` et des `Badges` attribués uniquement suite à des actions vérifiées.  
**Conséquences :** Confiance préservée, protection contre le farming de likes.

---

### ADR-007 : Frontend Angular Standalone avec Signals et Design System Dédié
**Contexte :** Le frontend doit être rapide, accessible, maintenable et évolutif vers le mobile.  
**Décision :** Angular 18+ en mode **Standalone Components**, réactivité via **Signals**, SCSS avec tokens HSL, sans bibliothèques de composants tierces lourdes.  
**Conséquences :** Bundle léger, contrôle total sur l'UI, accessibilité WCAG 2.1 AA.

---

### ADR-008 : UUID comme Identifiant Standard Universel des Entités
**Contexte :** Les identifiants numériques auto-incrémentés (`Long`) exposent des risques d'énumération non sécurisée et compliquent la synchronisation ou le partitionnement futur des bases de données.  
**Décision :** Toutes les entités persistantes de Mohsinon utilisent des identifiants **UUID v4 / RFC 4122** définis dans `BaseEntity`.  
**Conséquences :** Sécurité renforcée, génération décentralisée des clés, préparation optimale au sharding et à la scalabilité.

---

### ADR-009 : Architecture en Ledger Immuable pour les Points d'Impact (`ImpactTransaction`)
**Contexte :** Stocker un simple entier `user.impactPoints` modifiable directement par divers services génère des pertes de traçabilité et des corruptions.  
**Décision :** Chaque variation de points d'impact est enregistrée sous forme de transaction immuable dans une table de registre d'audit (`ImpactTransaction`).  
**Conséquences :** Traçabilité et auditabilité totales de l'origine de chaque point, intégrité historique garantie.

---

### ADR-010 : Stockage Haché (SHA-256) & Rotation Automatique des Refresh Tokens avec Détection de Fraude
**Contexte :** Les Refresh Tokens sont des secrets sensibles à longue durée de vie (7 jours). Les stocker en clair en base expose à un vol massif en cas d'accès direct à la base de données. De plus, un token volé pourrait être réutilisé si aucune politique de rotation n'est en place.  
**Décision :** 
1. Le Refresh Token brut (opaque 48-bytes Base64URL) est envoyé au client mais seule son empreinte **SHA-256** est persistée en base.
2. À chaque requête de rafraîchissement (`/api/v1/auth/refresh`), l'ancien token est immédiatement révoqué et marqué avec le hash du nouveau token émis (`replacedByTokenHash`).
3. Si une requête présente un token déjà remplacé ou révoqué, une **tentative de vol/réutilisation est détectée** : toutes les sessions actives de l'utilisateur sont immédiatement révoquées et l'accès est bloqué.  
**Conséquences :** Protection maximale contre les fuites de données et détection proactive des attaques par rejeu.

---

### ADR-011 : Abstraction `CurrentUserProvider` pour le Découplage de la Sécurité
**Contexte :** Faire appel directement à `SecurityContextHolder.getContext().getAuthentication()` dans les services métier introduit un couplage fort avec le framework Spring Security et complique les tests unitaires.  
**Décision :** Définition de l'interface `CurrentUserProvider` (`getCurrentUserId()`, `getCurrentUser()`, `requireCurrentUserId()`) injectée dans les services et contrôleurs.  
**Conséquences :** Code métier pur et facilement testable avec des mocks ou des contextes simulés.

---

### ADR-012 : Modèle d'Autorisation Hybride Tridimensionnel & Gouvernance Locale Autonome
**Contexte :** L'autorité locale de chaque mosquée (Imam, Président, Trésorier, Membres du Comité) est au cœur du modèle communautaire de Mohsinon. Un modèle RBAC plat accorderait des privilèges globaux indésirables (ex: un Imam de la Mosquée A pouvant administrer la Mosquée B).  
**Décision :** Adoption d'un modèle d'autorisation tridimensionnel :
$$\text{Rôle Global (Identité)} \times \text{Membership Local (Position dans un lieu)} \times \text{Permission Granulaire (Action)}$$
Le pipeline d'évaluation applique le principe **Deny by Default** : l'accès à une mosquée requiert expressément un `Membership` actif pour cette mosquée précise (`ResourceContext.mosque(mosqueId)`).  
**Conséquences :** Isolation stricte et prouvée entre mosquées. Gouvernance locale autonome sans prolifération de rôles système globaux.

---

### ADR-013 : Résolution Statique en Mémoire des Permissions et Intégration Déclarative SpEL (`@authz`)
**Contexte :** Persister les permissions et leurs mappings dans des tables de base de données relationnelle engendre des jointures SQL coûteuses à chaque requête HTTP et expose à des modifications accidentelles non versionnées. Par ailleurs, écrire des vérifications de sécurité impératives dans les contrôleurs disperse la logique.  
**Décision :**
1. Les permissions (`PermissionType`) et leurs matrices d'attribution (`PermissionRegistry`) sont des structures Java immuables résolues en mémoire sans coût SQL.
2. Seules les données dynamiques (`user_global_roles`, `memberships`) sont persistées en base avec indexation optimisée.
3. Un évaluateur Spring `@Component("authz")` expose des méthodes minces utilisables dans `@PreAuthorize` (ex: `@PreAuthorize("@authz.canManageMosque(principal, #mosqueId)")`).  
**Conséquences :** Performance optimale (zéro jointure pour la résolution de permissions), typage fort au build, et contrôleurs REST propres et déclaratifs.
