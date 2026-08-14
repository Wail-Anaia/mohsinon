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

---

### ADR-001 : Choix du modèle Modular Monolith plutôt que Microservices

**Contexte :**  
Mohsinon a vocation à devenir une infrastructure mondiale. Le réflexe courant est de démarrer directement en architecture microservices distribuée.  
**Décision :**  
Nous adoptons un **Modular Monolith** avec Spring Boot 3.x et Java 17. Le code est partitionné en modules métier étanches (`com.mohsinon.modules.*`), communiquant via des interfaces publiques et des événements de domaine.  
**Conséquences :**
- **Avantages :** Zéro surcoût d'orchestration distribuée, cohérence transactionnelle ACID native, déploiement simple sur un serveur ou conteneur unique, développement rapide sans latence réseau inter-services.
- **Risques maîtrisés :** Les frontières de modules interdisent les requêtes transversales non autorisées, ce qui permettra une extraction ultérieure en microservices si le trafic ou l'organisation l'exige.

---

### ADR-002 : Approche Core-First et Inversion de Dépendances

**Contexte :**  
Chaque module métier a besoin de sécurité, d'audit, de pagination, de gestion d'erreurs et d'entités communes.  
**Décision :**  
Nous développons d'abord le noyau commun `com.mohsinon.core`. Les modules métier dépendent du Core, mais le Core n'a aucune dépendance envers les modules métier.  
**Conséquences :**
- Réutilisabilité maximale, cohérence de l'API REST (format RFC 7807 uniforme), stabilité architecturale garantie.

---

### ADR-003 : Modèle d'Autorisation Découplé (Rôles vs Permissions vs Contexte)

**Contexte :**  
Dans une communauté, un utilisateur peut être un simple membre globalement, mais Imam ou responsable financier dans une mosquée précise. Lier les pouvoirs à des rôles système rigides (`ROLE_IMAM`) créerait des failles ou des rigidités.  
**Décision :**  
Nous découplons strictement :
1. Les **Rôles Globaux** (`USER`, `ADMIN`).
2. Les **Permissions Granulaires** (`MOSQUE_VERIFY`, `DONATION_MANAGE`, `INITIATIVE_APPROVE`).
3. Les **Appartenances Contextuelles** (`MosqueMember` avec rôle local).  
**Conséquences :**
- Évolutivité totale, sécurité granulaire, respect du principe de moindre privilège.

---

### ADR-004 : Modèle de Don Multi-Ressources Extensible

**Contexte :**  
Les plateformes caritatives se limitent souvent à la collecte de fonds en ligne. Mohsinon veut encourager le partage de biens matériels, de nourriture, d'outils, de compétences et de temps.  
**Décision :**  
L'entité `DonationItem` est générique et polymorphique via une catégorie (`DonationCategory`) et des attributs souples, plutôt qu'une table dédiée par type de ressource.  
**Conséquences :**
- Ajout immédiat de nouveaux types de dons sans migration lourde de base de données.

---

### ADR-005 : Abstraction Géographique Haversine vers PostGIS

**Contexte :**  
La recherche de proximité (mosquées, bénévoles, besoins) est essentielle. Cependant, imposer PostGIS dès le premier jour de développement local complique le setup des développeurs sans base spatiale locale.  
**Décision :**  
Nous créons une abstraction `GeoLocationService` / `GeoLocation` Value Object. L'implémentation par défaut utilise la formule de Haversine (compatible H2/PostgreSQL sans extension spatiale obligatoire). En production, une implémentation PostGIS native avec index spatiaux `GIST` s'activera de manière transparente via Spring Profile.  
**Conséquences :**
- Démarrage local immédiat à zéro friction tout en garantissant des performances optimales à grande échelle.

---

### ADR-006 : Moteur de Réputation Basé sur des Preuves Réelles (Non-Crypto)

**Contexte :**  
L'impact communautaire doit être valorisé et mesuré sans tomber dans la spéculation financière ou la complexité des blockchains/crypto-monnaies réelles.  
**Décision :**  
La réputation s'appuie sur des `Impact Points` et des `Badges` attribués uniquement suite à des actions vérifiées (don confirmé, bénévolat validé, jalon de projet atteint). L'unité interne (provisoirement appelée `Impact Coin`) est une mesure interne de confiance et de contribution sans valeur monétaire externe spéculative.  
**Conséquences :**
- Confiance préservée, protection contre le farming de likes ou la manipulation, simplicité technique.

---

### ADR-007 : Frontend Angular Standalone avec Signals et Design System Dédié

**Contexte :**  
Le frontend doit être rapide, accessible, maintenable et capable d'évoluer vers une application mobile ultérieurement.  
**Décision :**  
Utilisation d'Angular 18+ en mode **Standalone Components**, réactivité basée sur les **Signals**, SCSS avec tokens de design system HSL, sans bibliothèques de composants tierces lourdes et superflues.  
**Conséquences :**
- Performance maximale, bundle léger, contrôle total sur l'UX/UI, accessibilité WCAG 2.1 AA garantie.
