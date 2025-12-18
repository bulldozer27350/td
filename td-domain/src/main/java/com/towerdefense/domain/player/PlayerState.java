package com.towerdefense.domain.player;

public class PlayerState {

    private final PlayerId id;
    private int gold;
    private int lives;

    public PlayerState(PlayerId id, int gold, int lives) {
        this.id = id;
        this.gold = gold;
        this.lives = lives;
    }

    public PlayerId id() { return id; }
    public int gold() { return gold; }
    public int lives() { return lives; }

    public boolean canAfford(int cost) {
        return gold >= cost;
    }

    public void spendGold(int amount) {
        gold -= amount;
    }

    public void earnGold(int amount) {
        gold += amount;
    }

    public void loseLife() {
        lives--;
    }

    public boolean isAlive() {
        return lives > 0;
    }
}
