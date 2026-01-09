# Configuration des entités du jeu

## Unités de temps

⚠️ **Important** : Toutes les durées dans les fichiers de configuration sont exprimées en **secondes**.

Le moteur de jeu fonctionne en **ticks** (par défaut : 10 ticks = 1 seconde).
Les conversions sont automatiques.

## Fichiers de configuration

### `enemies.json`
- `speed` : vitesse de déplacement en **cases par seconde** (ex: `0.5` = une demi-case par seconde)
- Sera convertie automatiquement en cases/tick par le moteur

### `towers.json`
- `reloadSeconds` : temps de rechargement en **secondes** (ex: `2.0` = 2 secondes)
- `buildTimeTicks` : temps de construction en **ticks** (ex: `10` = 1 seconde avec 10 ticks/sec)
- Sera converti automatiquement par le moteur

### Exemples
```json
// Ennemi rapide : 2 cases par seconde
{
  "id": "fast_goblin",
  "hp": 50,
  "speed": 2.0,
  "bounty": 10
}

// Tour avec rechargement rapide
{
  "level": 1,
  "reloadSeconds": 0.5,
  "buildTimeTicks": 5
}
```