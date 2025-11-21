# 📄 CV MANAGER - Architecture des Applications

## 🎯 Objectif du Projet

L'objectif principal est de développer une application web robuste en architecture Full-Stack pour la gestion sécurisée d'une liste de CVs. L'application doit supporter la consultation publique des CVs et la modification/création d'utilisateurs via un système d'authentification par jeton JWT.

L'objectif de performance à terme est de gérer environ **100 000 CVs**.

## 🛠️ Technologies & Stack Actuelle

| Composant | Technologie | Notes |
| :--- | :--- | :--- |
| **Backend (API)** | Java 21, Spring Boot 3.x, Spring Security | Fournit l'API REST sécurisée. |
| **Persistance** | Spring Data JPA, H2 Database | Utilisation d'une base de données **en mémoire** pour le développement et le rendu final. |
| **Sécurité** | JWT (jjwt), BCryptPasswordEncoder | Authentification sans état. |
| **Mapping** | Lombok, ModelMapper | Utilisation de **classes Lombok mutables** pour les DTOs afin de simplifier le mapping avec ModelMapper. |
| **Frontend** | VueJS 3, Vue Router, Bootstrap 5 | En cours de développement. |

***

## 🟢 Statut Actuel du Projet (Backend 100% stable)

Les étapes 1, 2 et 3 sont terminées et validées par des tests unitaires et d'intégration.

### ✅ Fonctionnalités Complètes (Étapes 1, 2, 3)

* **Modèle de Données (Entités `Person`, `CV`, `Activity`) :** Complètement mappé et validé.
* **API CRUD :** Opérations complètes (POST, GET, PUT, DELETE) sur les entités Personne, CV et Activité.
* **Recherche Avancée :** Fonctionnalité de recherche des personnes par **Nom, Prénom, ou Titre d'Activité**.
* **API REST & DTO :** Les contrôleurs utilisent le mapping DTO ↔ Entité (`PersonDTO`, `CVDTO`, `ActivityDTO`) pour garantir l'isolation des couches.
* **Authentification JWT :** Le flux de connexion `/api/auth/login` et la vérification du jeton sont opérationnels.
* **Sécurité :** Tous les endpoints de modification (`POST`, `PUT`, `DELETE`) sont protégés par JWT.

### 🔑 Informations de Connexion (Peuplement)

Un utilisateur de test est injecté automatiquement à chaque démarrage du backend via le composant `@PostConstruct` dans `DataInitializer.java` :

| Champ | Valeur |
| :--- | :--- |
| **Email** | `test@amu.fr` |
| **Mot de Passe** | `motdepasse` |

***

## 💻 Instructions de Démarrage

### 1. Démarrage du Backend (API REST)

Le backend doit être démarré en premier pour accepter les requêtes du frontend.

1.  Assurez-vous d'être dans le répertoire `Cv Manager`.
2.  Lancez l'application Spring Boot :
    ```bash
    ./mvnw spring-boot:run
    ```
    (L'API est accessible sur `http://localhost:8080`)

### 2. Démarrage du Frontend (Application VueJS)

1.  Assurez-vous d'être dans le répertoire `cvmanager-frontend`.
2.  Lancez le serveur de développement :
    ```bash
    npm install  # Si les node_modules ont été supprimés
    npm run dev
    ```
    (Le frontend est accessible sur `http://localhost:5173` et se connecte à `http://localhost:8080`)

***

## 🚧 Tâches Restantes : Étape 4 (Frontend)

La phase d'architecture frontend (Phase 1) est terminée. Il reste à implémenter les fonctionnalités dans les vues pour compléter l'Étape 4.

| Phase         | Tâche | Composants / Endpoints | Note d'Accès |
|:--------------| :--- | :--- | :--- |
| **Phase 2.2** | Finalisation `CVList.vue` | Affichage des données (Personnes/CVs) et utilisation du champ de recherche. | Lecture Publique |
| **Phase 2.3** | **Modification/Édition de CV** | Implémentation complète de `CVEdit.vue` : Formulaire d'édition des détails de la personne et **CRUD complet des Activités** du CV. | **Protégé (PUT, DELETE)** |
| **Phase 3**   | **Packaging Final** | Configuration de `vite.config.js` (`base: '/frontend'`), `npm run build`, copie des fichiers statiques dans `src/main/resources/static/frontend/`, et génération du **`myapp.war`**. | **Tâche de rendu finale !** |

---
*Ce projet est sous Git. Veuilles à faire des commits fréquents et descriptifs lors du développement de ces fonctionnalités.*