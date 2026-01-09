package com.towerdefense.domain;

/**
 * Gestion centralisée du temps dans le jeu.
 * 
 * Le jeu fonctionne en "ticks" : chaque tick représente une unité de temps discrète.
 * Par défaut, 1 seconde = 10 ticks (configurable).
 * 
 * Cette classe permet de convertir entre secondes (configuration) et ticks (runtime).
 */
public final class GameTime {
    
    /**
     * Nombre de ticks par seconde de jeu.
     * Modifiable pour ajuster la granularité du jeu.
     */
    private static final int TICKS_PER_SECOND = 10;
    
    // Classe utilitaire : pas d'instanciation
    private GameTime() {
        throw new AssertionError("Utility class");
    }
    
    /**
     * Convertit une durée en secondes vers un nombre de ticks.
     * Arrondit à l'entier supérieur pour éviter les durées nulles.
     * 
     * @param seconds durée en secondes (peut être décimal)
     * @return nombre de ticks correspondant (toujours >= 1)
     */
    public static int secondsToTicks(double seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive: " + seconds);
        }
        return Math.max(1, (int) Math.ceil(seconds * TICKS_PER_SECOND));
    }
    
    /**
     * Convertit un nombre de ticks vers une durée en secondes.
     * 
     * @param ticks nombre de ticks
     * @return durée en secondes
     */
    public static double ticksToSeconds(int ticks) {
        if (ticks < 0) {
            throw new IllegalArgumentException("Ticks cannot be negative: " + ticks);
        }
        return ticks / (double) TICKS_PER_SECOND;
    }
    
    /**
     * Retourne le nombre de ticks par seconde configuré.
     * 
     * @return ticks par seconde
     */
    public static int getTicksPerSecond() {
        return TICKS_PER_SECOND;
    }
    
    /**
     * Calcule la vitesse en cases/tick à partir d'une vitesse en cases/seconde.
     * 
     * @param casesPerSecond vitesse en cases par seconde
     * @return vitesse en cases par tick
     */
    public static double casesPerSecondToCasesPerTick(double casesPerSecond) {
        return casesPerSecond / TICKS_PER_SECOND;
    }
}