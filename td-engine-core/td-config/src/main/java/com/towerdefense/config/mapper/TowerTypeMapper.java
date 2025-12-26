package com.towerdefense.config.mapper;

import java.util.List;

import com.towerdefense.config.dto.TowerLevelConfig;
import com.towerdefense.config.dto.TowerTypeConfig;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;
import com.towerdefense.domain.statik.tower.TowerType;

/** Mapper for converting TowerTypeConfig to TowerType domain model. */
public class TowerTypeMapper {

	/** Convert TowerTypeConfig to TowerType domain model. */
    public static TowerType toDomain(TowerTypeConfig config) {
        List<TowerLevelDefinition> levels =
            config.getLevels().stream()
                .map(TowerTypeMapper::toLevel)
                .toList();

        return new TowerType(config.getId(), levels);
    }

    /** Convert TowerLevelConfig to TowerLevelDefinition domain model. */
    private static TowerLevelDefinition toLevel(TowerLevelConfig cfg) {
        return new TowerLevelDefinition(
            cfg.getLevel(),
            cfg.getUpgradeCost(),
            cfg.getSellValue(),
            cfg.getRange(),
            cfg.getDamage(),
            cfg.getReloadSeconds(),
            cfg.getBuildTimeTicks()
        );
    }
}


