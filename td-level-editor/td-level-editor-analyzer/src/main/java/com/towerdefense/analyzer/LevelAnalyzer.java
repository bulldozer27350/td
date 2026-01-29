package com.towerdefense.analyzer;

import com.towerdefense.analyzer.model.LevelAnalysisReport;
import com.towerdefense.analyzer.model.LevelAnalysisReport.Severity;
import com.towerdefense.editor.api.model.draft.*;

import java.util.*;

public class LevelAnalyzer {
    
    private final Map<String, EditableEnemyType> enemyTypeRegistry;
    private final Map<String, EditableTowerType> towerTypeRegistry;
    
    public LevelAnalyzer(
            List<EditableEnemyType> enemyTypes,
            List<EditableTowerType> towerTypes) {
        
        this.enemyTypeRegistry = new HashMap<>();
        for (EditableEnemyType enemy : enemyTypes) {
            enemyTypeRegistry.put(enemy.getId(), enemy);
        }
        
        this.towerTypeRegistry = new HashMap<>();
        for (EditableTowerType tower : towerTypes) {
            towerTypeRegistry.put(tower.id(), tower);
        }
    }
    
    public LevelAnalysisReport analyze(EditableLevel level) {
        
        // 1. Calculer les métriques
        double economyScore = calculateEconomyScore(level);
        double pathScore = calculatePathScore(level);
        double coverageScore = calculateCoverageScore(level);
        double waveScore = calculateWaveScore(level);
        double livesScore = calculateLivesScore(level);
        
        // 2. Calculer la difficulté globale (moyenne pondérée)
        int difficultyScore = (int) (
            economyScore * 0.30 +
            pathScore * 0.20 +
            coverageScore * 0.25 +
            waveScore * 0.15 +
            livesScore * 0.10
        );
        
        LevelAnalysisReport report = new LevelAnalysisReport(difficultyScore);
        
        // 3. Ajouter les métriques
        report.addMetric("Economy Score", economyScore, "points");
        report.addMetric("Path Score", pathScore, "points");
        report.addMetric("Coverage Score", coverageScore, "points");
        report.addMetric("Wave Score", waveScore, "points");
        report.addMetric("Lives Score", livesScore, "points");
        
        // 4. Générer les warnings
        generateWarnings(level, report, economyScore, pathScore, coverageScore);
        
        return report;
    }
    
    // ===============================================================
    // ECONOMY SCORE : Argent vs coût minimum de défense
    // ===============================================================
    private double calculateEconomyScore(EditableLevel level) {
        int startingMoney = level.getStartingMoney();
        
        // Coût minimum : une tour niveau 1
        int minTowerCost = towerTypeRegistry.values().stream()
            .flatMap(t -> t.ranks().stream())
            .filter(u -> u.rank() == 1)
            .mapToInt(EditableTowerRank::cost)
            .min()
            .orElse(100);
        
        if (startingMoney == 0) return 100; // Impossible sans argent
        
        double ratio = (double) minTowerCost / startingMoney;
        
        // Plus le ratio est élevé, plus c'est difficile
        return Math.min(100, ratio * 100);
    }
    
    // ===============================================================
    // PATH SCORE : Longueur du chemin vs vitesse des ennemis
    // ===============================================================
    private double calculatePathScore(EditableLevel level) {
        if (level.getPaths().isEmpty()) return 100;
        
        EditablePath firstPath = level.getPaths().get(0);
        double pathLength = calculatePathLength(firstPath);
        
        // Vitesse moyenne des ennemis
        double avgEnemySpeed = level.getAttacks().stream()
            .flatMap(a -> a.getWaves().stream())
            .map(w -> enemyTypeRegistry.get(w.getEnemyType()))
            .filter(Objects::nonNull)
            .mapToDouble(EditableEnemyType::getSpeed)
            .average()
            .orElse(1.0);
        
        // Temps disponible = pathLength / avgSpeed
        double timeAvailable = pathLength / avgEnemySpeed;
        
        // Si < 10 secondes (600 ticks), très difficile
        if (timeAvailable < 600) return 90;
        if (timeAvailable < 1200) return 60;
        if (timeAvailable < 1800) return 30;
        return 10;
    }
    
    private double calculatePathLength(EditablePath path) {
        double length = 0;
        var points = path.points();
        for (int i = 0; i < points.size() - 1; i++) {
            var p1 = points.get(i);
            var p2 = points.get(i + 1);
            length += Math.sqrt(
                Math.pow(p2.x() - p1.x(), 2) +
                Math.pow(p2.y() - p1.y(), 2)
            );
        }
        return length;
    }
    
    // ===============================================================
    // COVERAGE SCORE : Cases du chemin couvertes par les tours
    // ===============================================================
    private double calculateCoverageScore(EditableLevel level) {
        if (level.getPaths().isEmpty()) return 100;
        
        EditablePath path = level.getPaths().get(0);
        Set<String> pathCells = extractPathCells(path);
        
        // Range max des tours disponibles
        double maxRange = level.getTowerCapacities().stream()
        		.map(TowerCapacity::getTowerTypeId)
            .map(towerTypeRegistry::get)
            .filter(Objects::nonNull)
            .flatMap(t -> t.ranks().stream())
            .mapToDouble(EditableTowerRank::range)
            .max()
            .orElse(0.0);
        
        if (maxRange == 0) return 100;
        
        // Estimation simpliste : combien de tours seraient nécessaires
        int mapWidth = level.getMap().getWidth();
        int mapHeight = level.getMap().getHeight();
        int totalCells = mapWidth * mapHeight - pathCells.size();
        
        // Nombre théorique de tours pour couvrir tout le chemin
        int theoreticalTowers = (int) Math.ceil(pathCells.size() / (Math.PI * maxRange * maxRange));
        
        if (theoreticalTowers > totalCells) return 95; // Pas assez d'espace
        if (theoreticalTowers > totalCells / 2) return 70;
        return 30;
    }
    
    private Set<String> extractPathCells(EditablePath path) {
        Set<String> cells = new HashSet<>();
        var points = path.points();
        for (int i = 0; i < points.size() - 1; i++) {
            var from = points.get(i);
            var to = points.get(i + 1);
            cells.addAll(getCellsBetween(from, to));
        }
        return cells;
    }
    
    private Set<String> getCellsBetween(
            com.towerdefense.editor.api.model.exportable.PositionDefinition from,
            com.towerdefense.editor.api.model.exportable.PositionDefinition to) {
        Set<String> cells = new HashSet<>();
        
        int x1 = from.x(), y1 = from.y();
        int x2 = to.x(), y2 = to.y();
        
        if (x1 == x2) {
            int minY = Math.min(y1, y2);
            int maxY = Math.max(y1, y2);
            for (int y = minY; y <= maxY; y++) {
                cells.add(x1 + "," + y);
            }
        } else if (y1 == y2) {
            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            for (int x = minX; x <= maxX; x++) {
                cells.add(x + "," + y1);
            }
        }
        
        return cells;
    }
    
    // ===============================================================
    // WAVE SCORE : Densité et timing des vagues
    // ===============================================================
    private double calculateWaveScore(EditableLevel level) {
        int totalEnemies = level.getAttacks().stream()
            .flatMap(a -> a.getWaves().stream())
            .mapToInt(EditableWave::getCount)
            .sum();
        
        if (totalEnemies == 0) return 0;
        if (totalEnemies < 10) return 20;
        if (totalEnemies < 50) return 50;
        if (totalEnemies < 100) return 75;
        return 95;
    }
    
    // ===============================================================
    // LIVES SCORE : Nombre de vies vs ennemis
    // ===============================================================
    private double calculateLivesScore(EditableLevel level) {
        int lives = level.getStartingLives();
        int totalEnemies = level.getAttacks().stream()
            .flatMap(a -> a.getWaves().stream())
            .mapToInt(EditableWave::getCount)
            .sum();
        
        if (lives >= totalEnemies) return 0; // Trivial
        
        double ratio = (double) totalEnemies / lives;
        
        if (ratio > 100) return 100;
        if (ratio > 50) return 80;
        if (ratio > 20) return 60;
        return 40;
    }
    
    // ===============================================================
    // WARNINGS
    // ===============================================================
    private void generateWarnings(
            EditableLevel level,
            LevelAnalysisReport report,
            double economyScore,
            double pathScore,
            double coverageScore) {
        
        // Avertissement économie
        if (economyScore > 80) {
            report.addWarning(
                "Economy",
                "Starting money is very low. Players may not afford any tower.",
                Severity.CRITICAL
            );
        }
        
        // Avertissement chemin
        if (pathScore > 80) {
            report.addWarning(
                "Path",
                "Path is very short or enemies are very fast. Very hard to defend.",
                Severity.WARNING
            );
        }
        
        // Avertissement couverture
        if (coverageScore > 80) {
            report.addWarning(
                "Coverage",
                "Not enough space to place towers to cover the path.",
                Severity.CRITICAL
            );
        }
        
        // Pas de chemins
        if (level.getPaths().isEmpty()) {
            report.addWarning(
                "Configuration",
                "No paths defined. Level is unplayable.",
                Severity.CRITICAL
            );
        }
        
        // Pas d'attaques
        if (level.getAttacks().isEmpty()) {
            report.addWarning(
                "Configuration",
                "No attacks defined. Level has no enemies.",
                Severity.WARNING
            );
        }
        
        // Pas de tours disponibles
        if (level.getTowerCapacities().isEmpty()) {
            report.addWarning(
                "Configuration",
                "No tower types available. Players cannot defend.",
                Severity.CRITICAL
            );
        }
    }
}