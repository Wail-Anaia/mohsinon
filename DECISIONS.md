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
**Conséquences :**
- Sécurité renforcée (impossibilité de deviner ou énumérer les IDs).
- Indépendance vis-à-vis de la base de données pour la génération des clés primaires.
- Préparation optimale à la réplication, au sharding et à l'éventuelle extraction en services distribués.

---

### ADR-009 : Architecture en Ledger Immuable pour les Points d'Impact (`ImpactTransaction`)
**Contexte :** Stocker un simple entier `user.impactPoints` modifiable directement par divers services génère des pertes de traçabilité, des corruptions de données et des contestations d'audit.  
**Décision :** Chaque variation de points d'impact est enregistrée sous forme de transaction immuable dans une table de registre d'audit (`ImpactTransaction`) avec `userId`, `type` (`EARNED`, `SPENT`, `ADJUSTED`), `points`, `referenceType`, `referenceId`, `reason` et `createdAt`. Le solde est dérivé de la somme des transactions ou matérialisé sous contrôle strict.  
**Conséquences :**
- Traçabilité et auditabilité totales de l'origine de chaque point.
- Répudiation impossible et intégrité historique garantie.
- Zéro point fantôme ou arbitraire.
