# 🎲 Property-Based Testing - Guide d'utilisation

## 🎯 Qu'est-ce que c'est ?

Le **Property-Based Testing** (PBT) génère automatiquement des données de test aléatoires pour vérifier que vos invariants métier sont TOUJOURS respectés, quelle que soit la configuration.

### Différence avec les tests classiques

```
Tests classiques :
  ✓ Avec 100$ et 3 vies, le jeu fonctionne
  ✓ Avec 200$ et 5 vies, le jeu fonctionne
  → Mais qu'en est-il de 137$ et 8 vies ?

Property-based testing :
  ✓ Avec N'IMPORTE QUELLE combinaison valide, le jeu fonctionne
  → Teste automatiquement 50-200 combinaisons aléatoires
```

---

## 🚀 Lancer les tests

### Tous les tests property-based
```bash
mvn test -Dtest=GameInvariantsPropertyTest
```

### Un test spécifique
```bash
mvn test -Dtest=GameInvariantsPropertyTest#player_gold_is_always_non_negative
```

### Avec plus d'itérations (recommandé pour CI/CD)
```bash
# Dans le code, modifier @Property(tries = 50) → @Property(tries = 200)
```

---

## 🐛 Que faire quand un test échoue ?

### 1. **Le test sauvegarde automatiquement la config**

```
❌ ÉCHEC ENREGISTRÉ : target/test-failures/player_gold_20240109_143022.json
   Pour rejouer : TestFailureReplayer.replay("player_gold_20240109_143022.json")
```

### 2. **Lister tous les échecs**

```bash
mvn test -Dtest=TestFailureReplayer
```

Ou en code :
```java
TestFailureRecorder.listFailures();
```

### 3. **Rejouer un échec spécifique**

```bash
mvn test -Dtest=TestFailureReplayer \
  -Dfailure.file=player_gold_20240109_143022.json
```

Ou en code :
```java
TestFailureReplayer.replay("player_gold_20240109_143022.json");
```

### 4. **Analyser la configuration problématique**

Le fichier JSON contient :
```json
{
  "testName": "player_gold_is_always_non_negative",
  "timestamp": "20240109_143022",
  "errorType": "java.lang.AssertionError",
  "errorMessage": "L'or est devenu négatif au tick 42 : -15",
  "stackTrace": "...",
  "config": {
    "levelConfig": {
      "id": "level_xyz",
      "startingLives": 3,
      "startingMoney": 87,
      ...
    }
  }
}
```

### 5. **Corriger le bug et vérifier**

Après correction :
```bash
# Rejoue tous les échecs pour vérifier qu'ils sont résolus
TestFailureReplayer.replayAll();
```

---

## ✍️ Écrire vos propres tests property-based

### Template de base

```java
@Property(tries = 50, shrinking = ShrinkingMode.FULL)
@Label("Description claire de l'invariant")
void mon_invariant(
        @ForAll("validLevelConfigs") LevelConfig levelConfig) {
    
    try {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .withLevel(levelConfig)
                .build();

        // Simule le jeu
        for (int i = 0; i < 500; i++) {
            engine.tick();
            
            // ✅ Vérifie l'invariant
            assertTrue(
                monInvariant(engine.getState()),
                "Message d'erreur clair"
            );
            
            if (engine.isGameOver()) break;
        }

    } catch (AssertionError | Exception e) {
        // Sauvegarde automatique
        GameConfig problemConfig = new GameConfig(levelConfig, null, null, null);
        TestFailureRecorder.recordFailure("mon_invariant", problemConfig, e);
        throw e;
    }
}
```

### Créer un générateur personnalisé

```java
@Provide
Arbitrary<LevelConfig> monGenerateur() {
    return Combinators.combine(
        Arbitraries.integers().between(1, 10),
        Arbitraries.integers().between(50, 300)
    ).as((lives, gold) -> {
        LevelConfig config = new LevelConfig();
        config.setStartingLives(lives);
        config.setStartingMoney(gold);
        // ...
        return config;
    });
}
```

---

## 🎛️ Configuration avancée

### Nombre d'itérations

```java
@Property(tries = 100)  // Par défaut : 50
```

**Recommandation** :
- Dev local : 50
- CI/CD : 200-500
- Avant release : 1000+

### Shrinking (réduction des cas)

```java
@Property(shrinking = ShrinkingMode.FULL)
```

Le **shrinking** trouve le cas MINIMAL qui reproduit le bug.

Exemple :
```
Config initiale qui échoue : {lives: 8, gold: 237, attacks: 5}
Après shrinking          : {lives: 1, gold: 50, attacks: 1}
                            ↑ Cas minimal qui reproduit le bug
```

### Seed pour reproduire exactement

```java
@Property(seed = "4242")
```

Utile pour :
- Debugging
- CI/CD déterministe
- Partage de bugs

---

## 📊 Métriques et statistiques

jqwik fournit automatiquement :

```
GameInvariantsPropertyTest > player_gold_is_always_non_negative
  tries = 50 
  checks = 50 
  generation = RANDOMIZED 
  after-failure = SAMPLE_FIRST 
  edge-cases = NONE 
  seed = -1234567890
  
  → All 50 checks passed ✓
```

---

## 🎯 Bonnes pratiques

### ✅ À faire

1. **Testez des invariants, pas des valeurs exactes**
   ```java
   ✅ assertTrue(gold >= 0)
   ❌ assertEquals(100, gold)
   ```

2. **Gardez les tests courts** (< 500 ticks)
   - Tests rapides = feedback rapide
   - Utilisez `MAX_TICKS` pour éviter les timeouts

3. **Messages d'erreur informatifs**
   ```java
   assertTrue(
       gold >= 0,
       String.format("Or négatif au tick %d : %d", tick, gold)
   );
   ```

4. **Sauvegardez TOUJOURS les échecs**
   - Permet le debugging
   - Constitue une base de régression

### ❌ À éviter

1. **Tests trop lents** (> 30s par test)
   - Réduire `tries` ou optimiser la simulation

2. **Générateurs invalides**
   ```java
   ❌ Arbitraries.integers() // Peut générer Integer.MIN_VALUE
   ✅ Arbitraries.integers().between(1, 100)
   ```

3. **Oublier le try-catch**
   - Sans ça, pas de sauvegarde des échecs

---

## 🔧 Troubleshooting

### "Le test est très lent"
- Réduire `tries` ou `MAX_TICKS`
- Profiler la simulation

### "Je n'arrive pas à reproduire"
- Vérifier que le fichier JSON est bien sauvegardé
- Utiliser `seed` pour forcer la reproductibilité

### "Trop de faux positifs"
- Affiner les contraintes du générateur
- Vérifier que l'invariant est correct

---

## 📚 Ressources

- [jqwik Documentation](https://jqwik.net/docs/current/user-guide.html)
- [Property-Based Testing (FP Complete)](https://www.fpcomplete.com/blog/2017/01/quickcheck/)
- [Hypothesis (Python equivalent)](https://hypothesis.readthedocs.io/)

---

## 🎓 Exemple complet

Voir `GameInvariantsPropertyTest.java` pour des exemples concrets de :
- Tests d'invariants métier
- Sauvegarde automatique des échecs
- Rejeu de configurations