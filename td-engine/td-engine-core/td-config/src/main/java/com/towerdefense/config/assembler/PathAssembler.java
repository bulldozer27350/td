package com.towerdefense.config.assembler;

import java.util.List;

import com.towerdefense.config.mapper.EnemyPathMapper;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.engine.api.model.configuration.PathsConfig;

/** Configuration class for mapping path configurations to domain models. */
public class PathAssembler {

	/**
	 * Maps the path configurations to a list of domain EnemyPath objects.
	 * 
	 * @param config The PathsConfig containing path configurations.
	 * @return A list of EnemyPath domain objects.
	 */
	public List<EnemyPath> enemyPaths(PathsConfig config) {
		return config.getPaths().stream().map(EnemyPathMapper::toDomain).toList();
	}
}
