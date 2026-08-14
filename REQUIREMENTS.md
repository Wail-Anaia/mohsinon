# EXIGENCES SYSTÈME (REQUIREMENTS) — MOHSINON

## 1. Exigences Fonctionnelles (Functional Requirements)

### 1.1 Module Identity & Access Management (IAM)
- **REQ-IAM-01 (Inscription & Authentification)** : Tout utilisateur peut créer un compte avec email unique, nom complet, mot de passe sécurisé et localisation approximative (ville/pays).
- **REQ-IAM-02 (Sécurité des Mots de Passe)** : Mots de passe hashés avec BCrypt (cost factor >= 12) ou Argon2. Longueur minimale 8 caractères avec complexité requise.
- **REQ-IAM-03 (Session & Tokens)** : Authentification basée sur JWT Access Token (15 min) et Refresh Token (7 jours) avec rotation automatique à chaque rafraîchissement et révocation immédiate en cas de compromission.
- **REQ-IAM-04 (RBAC & Permissions)** : Modèle d'autorisation découplant les Rôles (`USER`, `VOLUNTEER`, `DONOR`, `IMAM`, `MOSQUE_COMMITTEE`, `ADMIN`) et les Permissions individuelles (`CAN_CREATE_MOSQUE`, `CAN_VALIDATE_PROJECT`, etc.).

### 1.2 Module Mosques (Hubs Communautaires Locaux)
- **REQ-MSQ-01 (Fiche Mosquée)** : Enregistrement d'une mosquée avec nom, adresse physique, quartier, ville, pays, coordonnées GPS, capacité, description et contacts.
- **REQ-MSQ-02 (Gouvernance Locale)** : Désignation d'un Imam référent et d'un Comité de gestion rattaché à la mosquée avec permissions de validation des initiatives et de gestion des dons locaux.
- **REQ-MSQ-03 (Recherche de Proximité)** : Recherche des mosquées par nom, ville, code postal ou rayon kilométrique (formule Haversine / PostGIS).
- **REQ-MSQ-04 (Vérification)** : Statut de vérification de la mosquée (`PENDING`, `VERIFIED`, `REJECTED`) géré par les administrateurs pour garantir l'authenticité du lieu.

### 1.3 Module Donations (Bourse d'Entraide Multi-Ressources)
- **REQ-DON-01 (Types de Dons)** : Prise en charge des dons : `MONEY`, `CLOTHES`, `FOOD`, `FURNITURE`, `BOOKS`, `EQUIPMENT`, `MATERIAL`, `SERVICE`, `TIME`.
- **REQ-DON-02 (Cycle de Vie)** : Statuts : `DRAFT`, `AVAILABLE`, `RESERVED`, `COMPLETED`, `CANCELLED`.
- **REQ-DON-03 (Rattachement Mosquée)** : Possibilité de déposer un don physique à une mosquée partenaire comme point relais ou de faire un don direct entre particuliers.
- **REQ-DON-04 (Preuve de Réception)** : Confirmation bilatérale de la remise du don (donateur et bénéficiaire/comité) pour traçabilité et points d'impact.

### 1.4 Module Initiatives & Projets
- **REQ-INT-01 (Soumission de Besoin/Idée)** : Tout membre peut exprimer un besoin local ou proposer une initiative d'amélioration communautaire.
- **REQ-INT-02 (Validation Locale)** : Le comité de la mosquée locale ou un modérateur peut examiner, approuver et transformer l'initiative en projet officiel.
- **REQ-INT-03 (Jalons & Équipe)** : Définition des compétences requises, du calendrier, des jalons (`Milestones`) et affectation des bénévoles.
- **REQ-INT-04 (Mesure d'Impact)** : Téléversement de preuves de réalisation (comptes-rendus, photos vérifiées, indicateurs chiffrés).

### 1.5 Module Skills & Volunteers
- **REQ-SKL-01 (Référentiel des Compétences)** : Taxonomie des compétences (Bricolage, Plomberie, Électricité, Informatique, Traduction, Enseignement, Santé, Couture, Cuisine, etc.).
- **REQ-SKL-02 (Niveaux de Maîtrise)** : Niveaux : `BEGINNER`, `INTERMEDIATE`, `ADVANCED`, `EXPERT`, `MENTOR`, `MASTER`.
- **REQ-SKL-03 (Matching Bénévoles)** : Algorithme de mise en correspondance entre les besoins d'un projet/mosquée et les compétences disponibles dans la zone géographique.

### 1.6 Module Reputation & Impact
- **REQ-REP-01 (Points d'Impact)** : Attribution de points d'impact suite à des actions vérifiées (don livré, bénévolat validé, jalon de projet terminé).
- **REQ-REP-02 (Badges de Reconnaissance)** : Attribution de badges thématiques (`Bénévole Actif`, `Bâtisseur d'Impact`, `Mentor`, `Pilier Communautaire`).
- **REQ-REP-03 (Anti-Spam & Anti-Fraude)** : Interdiction formelle du vote artificiel ou du farming de réputation ; validation humaine ou bilatérale obligatoire.

---

## 2. Exigences Non-Fonctionnelles (Non-Functional Requirements)

### 2.1 Sécurité & Confidentialité (Privacy-by-Design & RGPD)
- **NFR-SEC-01** : Aucune adresse personnelle exacte ni coordonnée GPS de domicile ne doit être rendue publique sur l'API ou le frontend.
- **NFR-SEC-02** : Protection contre les attaques courantes (OWASP Top 10) : injections SQL, XSS, CSRF, brute force (Rate Limiting sur l'authentification).
- **NFR-SEC-03** : Journalisation d'audit immuable pour toutes les opérations de création, modification de droits et validations sensibles.

### 2.2 Performance & Scalabilité
- **NFR-PERF-01** : Temps de réponse de l'API REST < 200 ms pour 95% des requêtes en lecture.
- **NFR-PERF-02** : Pagination obligatoire sur toutes les collections de données (défaut : 20 éléments/page).
- **NFR-PERF-03** : Stateless backend facilitant le scaling horizontal futur.

### 2.3 Maintenabilité & Qualité de Code
- **NFR-MAINT-01** : Respect strict des principes SOLID, DRY, KISS et Clean Architecture.
- **NFR-MAINT-02** : Couverture de tests unitaires et d'intégration sur l'ensemble de la logique métier critique.
- **NFR-MAINT-03** : Documentation OpenAPI / Swagger tenue à jour et synchronisée avec le code.

### 2.4 Accessibilité & Ergonomie
- **NFR-UX-01** : Conformité aux directives d'accessibilité Web WCAG 2.1 niveau AA (contraste des couleurs, navigation clavier, sémantique HTML5).
- **NFR-UX-02** : Design 100% responsive (Mobile First, Tablette, Desktop).
