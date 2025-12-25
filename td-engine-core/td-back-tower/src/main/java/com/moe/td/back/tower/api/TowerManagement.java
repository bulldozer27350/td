package com.moe.td.back.tower.api;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.intentions.ShootIntention;

public interface TowerManagement {

	boolean canPlaceTower(GameState state, Position pos);
	
	boolean canShoot(GameState state, ShootIntention intention);
	
}
