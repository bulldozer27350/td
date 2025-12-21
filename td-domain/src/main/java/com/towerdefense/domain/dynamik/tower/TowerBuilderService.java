package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerType;

public interface TowerBuilderService {

	boolean canBuild(GameState state, PlayerState player, TowerType type, Position position);

	Tower build(GameState state, PlayerState player, TowerType type, Position position);

}