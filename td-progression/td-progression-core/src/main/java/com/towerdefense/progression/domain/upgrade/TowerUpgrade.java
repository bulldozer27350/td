package com.towerdefense.progression.domain.upgrade;

// Upgrade applicable à un niveau spécifique d'une tour spécifique
public class TowerUpgrade {
 private final String id;
 private final String name;
 private final String towerTypeId;  // ex: "archer"
 private final int towerRank;      // ex: 2 (pour archer niveau 2)
 private final int cost;            // coût en points d'upgrade
 private final UpgradeEffect effect;
 
 public TowerUpgrade(String id, String name, String towerTypeId, int towerRank, 
                     int cost, UpgradeEffect effect) {
     this.id = id;
     this.name = name;
     this.towerTypeId = towerTypeId;
     this.towerRank = towerRank;
     this.cost = cost;
     this.effect = effect;
 }
 
 // Vérifie si cet upgrade s'applique à ce niveau de tour
 public boolean appliesTo(String towerType, int rank) {
     return this.towerTypeId.equals(towerType) && this.towerRank == rank;
 }
 
 // Getters
 public String getId() { return id; }
 public String getName() { return name; }
 public String getTowerTypeId() { return towerTypeId; }
 public int getTowerRank() { return towerRank; }
 public int getCost() { return cost; }
 public UpgradeEffect getEffect() { return effect; }
}