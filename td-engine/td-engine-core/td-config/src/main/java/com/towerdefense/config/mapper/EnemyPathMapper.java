package com.towerdefense.config.mapper;

import java.util.List;

import com.towerdefense.domain.Position;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.engine.api.model.configuration.PathConfig;

/**
 * Mapper class to convert PathConfig DTOs to EnemyPath domain objects.
 */
public class EnemyPathMapper {

	/** Converts a PathConfig DTO to an EnemyPath domain object. */
    public static EnemyPath toDomain(PathConfig config) {
        List<Position> positions =
            config.getPoints().stream()
                .map(p -> new Position(p.getX(), p.getY()))
                .toList();

        return new EnemyPath(config.getId(), positions);
    }
}
