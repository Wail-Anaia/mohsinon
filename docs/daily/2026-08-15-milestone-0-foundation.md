# Journal de Bord — 15 Août 2026

**Objet :** Exécution du Milestone 0 (Inception, Documentation & Squelette de Dépôt)  
**Auteur :** Équipe d'Ingénierie Mohsinon  

---

## 1. Contexte & Objectifs de la Journée

L'objectif de cette première journée d'ingénierie est de poser les fondations normatives et architecturales du projet **Mohsinon** sans précipitation et conformément au principe directeur :  
**BUILD SMALL → VALIDATE → DOCUMENT → EXPAND**

---

## 2. Actions Réalisées

1. **Inspection de l'environnement local** :
   - Détection de Java JDK 17 LTS (`C:\Program Files\Java\jdk-17`).
   - Détection de Node.js v24.14.0 et NPM 11.9.0.
   - Détection de Git 2.52.0.
2. **Initialisation Git** :
   - Dépôt Git initialisé sur `main`.
   - Remote configuré vers `https://github.com/Wail-Anaia/mohsinon.git`.
   - Création du `.gitignore` pour isoler les artefacts de build, clés secrètes et fichiers temporaires.
3. **Rédaction du Corpus Documentaire** :
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
4. **Création des Dossiers Thématiques de Documentation** :
   - `docs/architecture/`
   - `docs/api/`
   - `docs/database/`
   - `docs/security/`
   - `docs/decisions/`
   - `docs/daily/`

---

## 3. Décisions Clés Enregistrées
- Choix validé d'un **Modular Monolith** avec Spring Boot 3 / Java 17 et Angular Standalone.
- Priorité métier donnée au Hub Mosquée (Imam & Comité) comme premier point d'ancrage territorial.
- Découplage strict des Rôles, Permissions et Appartenances contextuelles.
- Modèle générique et extensible de dons multi-ressources.
- Abstraction géographique propre (Haversine -> PostGIS).
- Moteur d'impact et de réputation basé sur des preuves concrètes et vérifiables (non-crypto).

---

## 4. Prochaines Étapes
- Validation par le Product Manager / Utilisateur de la finalisation du Milestone 0.
- Lancement du **Milestone 1 : Core + Configuration + BaseEntity + Exceptions + Database**.
