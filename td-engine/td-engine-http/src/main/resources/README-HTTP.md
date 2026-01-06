# Tower Defense HTTP API

## Structure des fichiers

```
td-engine-http/
├── src/main/java/com/towerdefense/http/
│   ├── TowerDefenseHttpApplication.java          # Point d'entrée Spring Boot
│   ├── config/
│   │   ├── GameRuntimeConfiguration.java         # Configuration du GameRuntime
│   │   └── WebConfiguration.java                 # Configuration CORS
│   └── controller/
│       ├── GameController.java                   # Contrôleur REST
│       ├── GameStateMapper.java                  # Mapper DTO <-> HTTP Model
│       └── GlobalExceptionHandler.java           # Gestion des erreurs
├── src/main/resources/
│   ├── application.yml                           # Configuration Spring Boot
│   └── openapi/
│       └── game-api.yaml                         # Spécification OpenAPI
└── pom.xml
```

## Démarrage du serveur

### 1. Compiler le projet

```bash
mvn clean install
```

### 2. Lancer le serveur

```bash
# Depuis le module td-engine-http
mvn spring-boot:run

# OU depuis la racine
mvn spring-boot:run -pl td-engine-http
```

Le serveur démarre sur **http://localhost:8080**

### 3. Accéder à la documentation

- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8080/api-docs

## Endpoints disponibles

### 🎮 État du jeu

**GET** `/game/state`
```bash
curl http://localhost:8080/game/state
```

Retourne l'état complet du jeu (tours, ennemis, projectiles, joueur).

---

**GET** `/game/status`
```bash
curl http://localhost:8080/game/status
```

Vérifie si la partie est terminée.

```json
{
  "gameOver": false
}
```

---

### ⏱️ Avancer le temps

**POST** `/game/tick`
```bash
curl -X POST http://localhost:8080/game/tick
```

Fait avancer le jeu d'un tick.

---

### 🗼 Gestion des tours

**POST** `/game/commands/place-tower`
```bash
curl -X POST http://localhost:8080/game/commands/place-tower \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": "05aebba5-4722-4a21-9e34-af9b8f33ee20",
    "towerType": "BASIC",
    "x": 5,
    "y": 3
  }'
```

---

**POST** `/game/commands/upgrade-tower`
```bash
curl -X POST http://localhost:8080/game/commands/upgrade-tower \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": "05aebba5-4722-4a21-9e34-af9b8f33ee20",
    "x": 5,
    "y": 3
  }'
```

---

**POST** `/game/commands/sell-tower`
```bash
curl -X POST http://localhost:8080/game/commands/sell-tower \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": "05aebba5-4722-4a21-9e34-af9b8f33ee20",
    "x": 5,
    "y": 3
  }'
```

---

## Configuration

### Port du serveur

Par défaut : **8080**

Pour changer :
```yaml
# application.yml
server:
  port: 9000
```

Ou via variable d'environnement :
```bash
SERVER_PORT=9000 mvn spring-boot:run
```

---

### CORS

⚠️ **La configuration actuelle autorise TOUTES les origines** (développement uniquement) !

Pour la production, modifier `WebConfiguration.java` :

```java
registry.addMapping("/**")
    .allowedOrigins("https://votre-frontend.com")  // Origine spécifique
    .allowedMethods("GET", "POST", "PUT", "DELETE")
    .allowedHeaders("*");
```

---

### GameRuntime

La configuration du `GameRuntime` se trouve dans `GameRuntimeConfiguration.java`.

**À personnaliser selon vos besoins** :
- Charger une configuration depuis un fichier
- Initialiser avec un niveau spécifique
- Configurer le joueur initial

```java
private GameConfig createDefaultConfig() {
    return GameConfig.builder()
        .playerId("player-123")
        .levelIndex(0)
        .attackIndex(0)
        .initialGold(500)
        .initialLives(20)
        .build();
}
```

---

## Tests avec Postman / Insomnia

1. Importer l'URL OpenAPI : `http://localhost:8080/api-docs`
2. Tous les endpoints seront automatiquement configurés

---

## Logs

Les logs sont configurés dans `application.yml` :

```yaml
logging:
  level:
    com.towerdefense: DEBUG  # Vos logs applicatifs
    org.springframework.web: INFO
```

---

## Dépannage

### Le serveur ne démarre pas

**Erreur** : `Port 8080 already in use`

**Solution** : Un autre processus utilise le port 8080
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

---

### GameRuntimeFactory introuvable

**Erreur** : `Cannot resolve symbol 'GameRuntimeFactory'`

**Solution** : Adapter `GameRuntimeConfiguration.java` selon votre implémentation :

```java
@Bean
public GameRuntime gameRuntime() {
    // Remplacer par votre factory ou constructeur
    return new YourGameRuntimeImpl();
}
```

---

### CORS bloqués en production

**Symptôme** : Requêtes bloquées depuis le navigateur

**Solution** : Configurer les origines autorisées dans `WebConfiguration.java`

---

## Intégration avec un frontend

### Exemple JavaScript

```javascript
// Récupérer l'état du jeu
const state = await fetch('http://localhost:8080/game/state')
  .then(res => res.json());

// Placer une tour
await fetch('http://localhost:8080/game/commands/place-tower', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    playerId: 'player-id',
    towerType: 'BASIC',
    x: 5,
    y: 3
  })
});

// Avancer le jeu
await fetch('http://localhost:8080/game/tick', { method: 'POST' });
```

---

## Production

### Générer le JAR exécutable

```bash
mvn clean package
```

Le JAR se trouve dans `target/td-engine-http-0.0.1-SNAPSHOT.jar`

### Exécuter le JAR

```bash
java -jar target/td-engine-http-0.0.1-SNAPSHOT.jar
```

### Variables d'environnement

```bash
export SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=prod
java -jar td-engine-http.jar
```