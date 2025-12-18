package com.moe.td.back.tower;

import com.moe.td.back.tower.api.TowerManagement;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.ShootIntention;

public class TowerBack implements TowerManagement {

	public boolean canPlaceTower(GameState state, Position pos) {
        return state.towers().stream().noneMatch(t -> t.position().equals(pos));
    }

    public boolean canShoot(GameState state, ShootIntention intention) {
        Tower tower = state.towers().stream()
                .filter(t -> t.id().equals(intention.towerId()))
                .findFirst().orElse(null);

        Enemy target = state.enemies().stream()
                .filter(e -> e.id().equals(intention.targetId()))
                .findFirst().orElse(null);

        if (tower == null || target == null) return false;
        if (!tower.isReady()) return false;
        return tower.canShootTarget(target.position());
    }

}
