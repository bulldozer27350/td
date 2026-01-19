package com.towerdefense.engine.tests.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.PathConfig;
import com.towerdefense.engine.api.model.configuration.PointConfig;
import com.towerdefense.engine.api.model.configuration.TowerLevelConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Provide;

/**
 * Générateurs de configurations aléatoires mais valides pour les tests
 * property-based.
 * 
 * ✅ VERSION COMPLÈTE avec méthodes helper pour créer les configs
 */
public class ConfigGenerators {

	// ========================
	// CONSTANTES DE DOMAINE
	// ========================

	private static final int MIN_STARTING_GOLD = 50;
	private static final int MAX_STARTING_GOLD = 500;

	private static final int MIN_STARTING_LIVES = 1;
	private static final int MAX_STARTING_LIVES = 20;

	private static final int MIN_ENEMY_HP = 10;
	private static final int MAX_ENEMY_HP = 500;

	private static final double MIN_ENEMY_SPEED = 0.1;
	private static final double MAX_ENEMY_SPEED = 5.0;

	private static final int MIN_BOUNTY = 1;
	private static final int MAX_BOUNTY = 100;

	private static final int MIN_TOWER_COST = 10;
	private static final int MAX_TOWER_COST = 200;

	private static final double MIN_TOWER_RANGE = 1.0;
	private static final double MAX_TOWER_RANGE = 8.0;

	private static final int MIN_TOWER_DAMAGE = 5;
	private static final int MAX_TOWER_DAMAGE = 100;

	private static final double MIN_RELOAD_TIME = 0.5;
	private static final double MAX_RELOAD_TIME = 5.0;

	private static final int MIN_BUILD_TIME = 1;
	private static final int MAX_BUILD_TIME = 50;

	// ========================
	// GÉNÉRATEURS DE BASE
	// ========================

	@Provide
	public Arbitrary<String> towerTypeIds() {
		return Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(12).map(s -> "tower_" + s.toLowerCase());
	}

	@Provide
	public Arbitrary<String> enemyTypeIds() {
		return Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(12).map(s -> "enemy_" + s.toLowerCase());
	}

	@Provide
	public Arbitrary<String> pathIds() {
		return Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(12).map(s -> "path_" + s.toLowerCase());
	}

	@Provide
	public Arbitrary<EnemyTypeConfig> enemyTypes() {
		return Combinators.combine(enemyTypeIds(), Arbitraries.integers().between(MIN_ENEMY_HP, MAX_ENEMY_HP),
				Arbitraries.doubles().between(MIN_ENEMY_SPEED, MAX_ENEMY_SPEED),
				Arbitraries.integers().between(MIN_BOUNTY, MAX_BOUNTY)).as((id, hp, speed, bounty) -> {
					EnemyTypeConfig config = new EnemyTypeConfig();
					config.setId(id);
					config.setHp(hp);
					config.setSpeed(speed);
					config.setBounty(bounty);
					return config;
				});
	}

	@Provide
	public Arbitrary<List<EnemyTypeConfig>> enemyTypesList() {
		return enemyTypes().list().ofMinSize(1).ofMaxSize(5);
	}

	@Provide
	public Arbitrary<TowerLevelConfig> towerLevels() {
		return Combinators
				.combine(Arbitraries.integers().between(1, 5),
						Arbitraries.integers().between(MIN_TOWER_COST, MAX_TOWER_COST),
						Arbitraries.integers().between(MIN_TOWER_COST / 2, MAX_TOWER_COST),
						Arbitraries.doubles().between(MIN_TOWER_RANGE, MAX_TOWER_RANGE),
						Arbitraries.integers().between(MIN_TOWER_DAMAGE, MAX_TOWER_DAMAGE),
						Arbitraries.doubles().between(MIN_RELOAD_TIME, MAX_RELOAD_TIME),
						Arbitraries.integers().between(MIN_BUILD_TIME, MAX_BUILD_TIME))
				.as((level, cost, sell, range, damage, reload, buildTime) -> {
					int sellValue = Math.min(sell, cost - 1);

					return new TowerLevelConfig(level, cost, sellValue, range, damage, reload, buildTime);
				});
	}

	@Provide
	public Arbitrary<TowerTypeConfig> towerTypes() {
		return Combinators.combine(towerTypeIds(), Arbitraries.integers().between(1, 3)).flatAs((id, levelCount) -> {
			return towerLevels().list().ofSize(levelCount).map(levelsList -> {
				TowerTypeConfig config = new TowerTypeConfig();
				config.setId(id);
				config.setName("Tower " + id);

				// Crée des niveaux avec coûts croissants
				List<TowerLevelConfig> sortedLevels = new ArrayList<>();
				int previousCost = MIN_TOWER_COST;
				int level = 1;

				for (TowerLevelConfig originalLevel : levelsList) {
					int newCost = Math.max(previousCost + 10, originalLevel.getUpgradeCost());
					int sellValue = (int) (newCost * 0.7);

					sortedLevels.add(new TowerLevelConfig(level++, newCost, sellValue, originalLevel.getRange(),
							originalLevel.getDamage(), originalLevel.getReloadSeconds(),
							originalLevel.getBuildTimeTicks()));

					previousCost = newCost;
				}

				config.setLevels(sortedLevels);
				return config;
			});
		});
	}

	@Provide
	public Arbitrary<List<TowerTypeConfig>> towerTypesList() {
		return towerTypes().list().ofMinSize(1).ofMaxSize(5);
	}

	@Provide
	public Arbitrary<PointConfig> points() {
		return Combinators.combine(Arbitraries.integers().between(0, 14), Arbitraries.integers().between(0, 14))
				.as((x, y) -> {
					PointConfig point = new PointConfig();
					point.setX(x);
					point.setY(y);
					return point;
				});
	}

	@Provide
	public Arbitrary<PathConfig> paths() {
		return Combinators.combine(pathIds(), points().list().ofMinSize(2).ofMaxSize(8)).as((id, pointsList) -> {
			PathConfig path = new PathConfig();
			path.setId(id);
			path.setPoints(pointsList);
			return path;
		});
	}

	@Provide
	public Arbitrary<List<PathConfig>> pathsList() {
		return paths().list().ofMinSize(1).ofMaxSize(3);
	}

	@Provide
	public Arbitrary<WaveConfig> waves() {
		return Combinators
				.combine(Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
						Arbitraries.integers().between(0, 100), Arbitraries.integers().between(5, 30),
						Arbitraries.integers().between(1, 20), enemyTypeIds(), pathIds())
				.as((id, startTick, interval, count, enemyType, pathId) -> new WaveConfig("wave_" + id, startTick,
						interval, count, enemyType, pathId));
	}

	@Provide
	public Arbitrary<AttackConfig> attacks() {
		return Combinators
				.combine(Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
						waves().list().ofMinSize(1).ofMaxSize(5))
				.as((id, wavesList) -> new AttackConfig("attack_" + id, wavesList));
	}

	@Provide
	public Arbitrary<LevelConfig> levelConfigs() {
		return Combinators
				.combine(Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
						attacks().list().ofMinSize(1).ofMaxSize(3),
						Arbitraries.integers().between(MIN_STARTING_LIVES, MAX_STARTING_LIVES),
						Arbitraries.integers().between(MIN_STARTING_GOLD, MAX_STARTING_GOLD))
				.as((id, attacksList, lives, gold) -> {
					LevelConfig config = new LevelConfig();
					config.setId("level_" + id);
					config.setAttacks(attacksList);
					config.setStartingLives(lives);
					config.setStartingMoney(gold);
					return config;
				});
	}

	// ========================
	// GÉNÉRATEUR PRINCIPAL AVEC COHÉRENCE
	// ========================

	/**
	 * Génère un GameConfig complet avec toutes les dépendances cohérentes.
	 * 
	 * ✅ IMPORTANT : Les références entre configs sont automatiquement fixées pour
	 * garantir la cohérence (enemyType, pathId, etc.)
	 */
	@Provide
	public Arbitrary<GameConfig> gameConfigs() {
		return Combinators.combine(levelConfigs(), pathsList(), towerTypesList(), enemyTypesList())
				.as((level, pathsList, towersList, enemiesList) -> {

					// ✅ ÉTAPE 1 : Fixer les références dans les waves
					List<AttackConfig> fixedAttacks = level.getAttacks().stream()
							.map(attack -> fixAttackReferences(attack, enemiesList, pathsList))
							.collect(Collectors.toList());

					level.setAttacks(fixedAttacks);

					// ✅ ÉTAPE 2 : Créer les configs avec les méthodes helper
					level.setPaths(pathsList);
					TowersConfig towersConfig = createTowersConfig(towersList);
					EnemiesConfig enemiesConfig = createEnemiesConfig(enemiesList);

					return new GameConfig(level, towersConfig, enemiesConfig);
				});
	}

	// ========================
	// ✅ MÉTHODES HELPER POUR CRÉER LES CONFIGS
	// ========================

	/**
	 * Crée un TowersConfig à partir d'une liste de TowerTypeConfig.
	 */
	private TowersConfig createTowersConfig(List<TowerTypeConfig> towersList) {
		TowersConfig config = new TowersConfig();

		try {
			// Accès via réflexion car pas de setter public
			var field = TowersConfig.class.getDeclaredField("towers");
			field.setAccessible(true);
			field.set(config, new ArrayList<>(towersList));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			// Fallback : si le champ n'existe pas ou n'est pas accessible,
			// on essaie le setter public (s'il existe)
			try {
				config.setTowers(towersList);
			} catch (Exception ex) {
				throw new RuntimeException(
						"Cannot initialize TowersConfig. Add public setTowers() method or make 'towers' field accessible",
						e);
			}
		}

		return config;
	}

	/**
	 * Crée un EnemiesConfig à partir d'une liste de EnemyTypeConfig.
	 */
	private EnemiesConfig createEnemiesConfig(List<EnemyTypeConfig> enemiesList) {
		EnemiesConfig config = new EnemiesConfig();

		try {
			// Accès via réflexion car pas de setter public
			var field = EnemiesConfig.class.getDeclaredField("enemies");
			field.setAccessible(true);
			field.set(config, new ArrayList<>(enemiesList));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			// Fallback : si le champ n'existe pas ou n'est pas accessible,
			// on essaie le setter public (s'il existe)
			try {
				config.setEnemies(enemiesList);
			} catch (Exception ex) {
				throw new RuntimeException(
						"Cannot initialize EnemiesConfig. Add public setEnemies() method or make 'enemies' field accessible",
						e);
			}
		}

		return config;
	}

	// ========================
	// MÉTHODES DE COHÉRENCE DES RÉFÉRENCES
	// ========================

	/**
	 * Fixe les références dans un AttackConfig pour garantir que : - Les enemyType
	 * référencent des ennemis existants - Les pathId référencent des chemins
	 * existants
	 */
	private AttackConfig fixAttackReferences(AttackConfig attack, List<EnemyTypeConfig> enemies,
			List<PathConfig> paths) {
		if (enemies.isEmpty() || paths.isEmpty()) {
			throw new IllegalArgumentException("Cannot fix attack references: enemies or paths list is empty");
		}

		List<WaveConfig> fixedWaves = attack.getWaves().stream().map(wave -> fixWaveReferences(wave, enemies, paths))
				.collect(Collectors.toList());

		return new AttackConfig(attack.getId(), fixedWaves);
	}

	/**
	 * Fixe les références dans un WaveConfig pour garantir la cohérence.
	 */
	private WaveConfig fixWaveReferences(WaveConfig wave, List<EnemyTypeConfig> enemies, List<PathConfig> paths) {
		// Utilise le premier ennemi et le premier chemin disponibles
		// (stratégie simple mais garantit la cohérence)
		String validEnemyType = enemies.get(0).getId();
		String validPathId = paths.get(0).getId();

		return new WaveConfig(wave.getId(), wave.getStartTick(), wave.getSpawnInterval(), wave.getCount(),
				validEnemyType, // ✅ Référence valide
				validPathId // ✅ Référence valide
		);
	}

	/**
	 * ✅ MÉTHODE ALTERNATIVE : Distribution aléatoire des références
	 * 
	 * Si vous voulez plus de diversité dans les références, vous pouvez utiliser
	 * cette version qui distribue les ennemis et chemins de manière plus variée.
	 */
	@SuppressWarnings("unused")
	private WaveConfig fixWaveReferencesWithRandomDistribution(WaveConfig wave, List<EnemyTypeConfig> enemies,
			List<PathConfig> paths) {
		// Utilise le hash de l'ID de la wave pour sélectionner de manière déterministe
		int enemyIndex = Math.abs(wave.getId().hashCode()) % enemies.size();
		int pathIndex = Math.abs(wave.getId().hashCode() + 1) % paths.size();

		String validEnemyType = enemies.get(enemyIndex).getId();
		String validPathId = paths.get(pathIndex).getId();

		return new WaveConfig(wave.getId(), wave.getStartTick(), wave.getSpawnInterval(), wave.getCount(),
				validEnemyType, validPathId);
	}
}