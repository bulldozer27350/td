package com.towerdefense.meta;

/**
 * Progression méta d’un type de tour.
 * Ces bonus sont appliqués lors de la création
 * d’une tour en jeu.
 */
public class TowerMetaProgress {

    private final String towerTypeId;

    private int damageBonusPercent = 0;
    private int rangeBonusPercent = 0;
    private int reloadBonusPercent = 0;

    public TowerMetaProgress(String towerTypeId) {
        this.towerTypeId = towerTypeId;
    }

    public String towerTypeId() {
        return towerTypeId;
    }

    public int damageBonusPercent() {
        return damageBonusPercent;
    }

    public int rangeBonusPercent() {
        return rangeBonusPercent;
    }

    public int reloadBonusPercent() {
        return reloadBonusPercent;
    }

    // -------------------------
    // Upgrade logic
    // -------------------------

    public void upgradeDamage(int percent) {
        this.damageBonusPercent += percent;
    }

    public void upgradeRange(int percent) {
        this.rangeBonusPercent += percent;
    }

    public void upgradeReload(int percent) {
        this.reloadBonusPercent += percent;
    }
}
