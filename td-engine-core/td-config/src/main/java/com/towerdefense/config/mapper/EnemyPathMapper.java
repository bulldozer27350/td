package com.towerdefense.config.mapper;

import java.util.List;

import com.towerdefense.config.dto.PathConfig;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.map.EnemyPath;

public class EnemyPathMapper {

    public static EnemyPath toDomain(PathConfig config) {
        List<Position> positions =
            config.getPoints().stream()
                .map(p -> new Position(p.getX(), p.getY()))
                .toList();

        return new EnemyPath(config.getId(), positions);
    }
}
