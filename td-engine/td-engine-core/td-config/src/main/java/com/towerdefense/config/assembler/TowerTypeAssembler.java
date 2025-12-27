package com.towerdefense.config.assembler;

import java.util.Map;
import java.util.stream.Collectors;

import com.towerdefense.config.mapper.TowerTypeMapper;
import com.towerdefense.config.registry.TowerTypeRegistry;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.engine.api.model.configuration.TowersConfig;

/** Configuration class for setting up the TowerTypeRegistry. */
public class TowerTypeAssembler {

	/** 
	 * Create and return a TowerTypeRegistry from the given TowersConfig. 
	 * 
	 * @param config The configuration containing tower type definitions.
	 * @return A TowerTypeRegistry populated with tower types from the config.
	 */
	public TowerTypeRegistry towerTypeRegistry(TowersConfig config) {
		Map<String, TowerType> types = config.getTowers().stream().map(TowerTypeMapper::toDomain)
				.collect(Collectors.toMap(TowerType::name, t -> t));

		return new TowerTypeRegistry(types);
	}
}
