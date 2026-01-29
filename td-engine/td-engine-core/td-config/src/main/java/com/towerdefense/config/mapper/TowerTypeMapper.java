package com.towerdefense.config.mapper;

import java.util.List;

import com.towerdefense.domain.statik.tower.TowerRankDefinition;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.engine.api.model.configuration.TowerRankConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

/** Mapper for converting TowerTypeConfig to TowerType domain model. */
public class TowerTypeMapper {

	/** Convert TowerTypeConfig to TowerType domain model. */
    public static TowerType toDomain(TowerTypeConfig config) {
        List<TowerRankDefinition> levels =
            config.getRanks().stream()
                .map(TowerTypeMapper::toLevel)
                .toList();

        return new TowerType(config.getId(), levels);
    }

    /** Convert TowerRankConfig to TowerRankDefinition domain model. */
    private static TowerRankDefinition toLevel(TowerRankConfig cfg) {
        return new TowerRankDefinition(
            cfg.getRank(),
            cfg.getUpgradeCost(),
            cfg.getSellValue(),
            cfg.getRange(),
            cfg.getDamage(),
            cfg.getReloadSeconds(),
            cfg.getBuildTimeTicks()
        );
    }
}


