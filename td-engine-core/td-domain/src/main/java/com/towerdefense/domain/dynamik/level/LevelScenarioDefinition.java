package com.towerdefense.domain.dynamik.level;

import java.util.List;

public class LevelScenarioDefinition {

    private final int id;
    private final List<AttackDefinition> attacks;

    public LevelScenarioDefinition(int id, List<AttackDefinition> attacks) {
        this.id = id;
        this.attacks = List.copyOf(attacks);
    }

    public int getId() { return id; }
    public List<AttackDefinition> getAttacks() { return attacks; }
}
