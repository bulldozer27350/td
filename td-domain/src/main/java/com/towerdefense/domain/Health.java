package com.towerdefense.domain;

public class Health {
    private final int max;
    private int current;

    public Health(int max) {
        this.max = max;
        this.current = max;
    }

    public int current() { return current; }
    public int max() { return max; }
    public boolean isDead() { return current <= 0; }

    public void applyDamage(int dmg) {
        current = Math.max(0, current - dmg);
    }
}