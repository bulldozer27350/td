package com.towerdefense.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Représente le profil global d’un joueur.
 * 
 * Ce profil est indépendant d’une partie en cours.
 * Il contient les données persistantes utilisées pour
 * influencer les parties futures (méta-progression).
 */
public class PlayerProfile {

    private final PlayerId playerId;

    /** Monnaie globale (hors partie) */
    private int metaGold;

    /** Vies supplémentaires débloquées hors niveau */
    private int extraLives;

    /** Tours débloquées par type */
    private final Set<String> unlockedTowerTypes;

    /**
     * Améliorations méta par type de tour.
     * La clé est un identifiant logique de TowerType (ex: "machine-gun").
     */
    private final Map<String, TowerMetaProgress> towerProgress = new HashMap<>();

    public PlayerProfile(PlayerId playerId,
                         int metaGold,
                         int extraLives,
                         Set<String> unlockedTowerTypes) {
        this.playerId = playerId;
        this.metaGold = metaGold;
        this.extraLives = extraLives;
        this.unlockedTowerTypes = unlockedTowerTypes;
    }

    public PlayerId playerId() {
        return playerId;
    }

    public int metaGold() {
        return metaGold;
    }

    public int extraLives() {
        return extraLives;
    }

    public boolean hasUnlockedTower(String towerTypeId) {
        return unlockedTowerTypes.contains(towerTypeId);
    }

    public Map<String, TowerMetaProgress> towerProgress() {
        return Collections.unmodifiableMap(towerProgress);
    }

    // -------------------------
    // Domain actions
    // -------------------------

    public void earnMetaGold(int amount) {
        if (amount <= 0) return;
        this.metaGold += amount;
    }

    public boolean spendMetaGold(int amount) {
        if (amount <= 0 || metaGold < amount) {
            return false;
        }
        metaGold -= amount;
        return true;
    }

    public TowerMetaProgress progressForTower(String towerTypeId) {
        return towerProgress.computeIfAbsent(
            towerTypeId,
            id -> new TowerMetaProgress(id)
        );
    }

    public void addExtraLife() {
        this.extraLives += 1;
    }
}
