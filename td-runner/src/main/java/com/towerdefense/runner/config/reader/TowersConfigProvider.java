package com.towerdefense.runner.config.reader;

import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

public class TowersConfigProvider implements DTOProvider<TowersConfig> {

	@Override
	public TowersConfig providesDTO() {
		JsonConfigLoader loader = new JsonConfigLoader();
		TowersConfig config = loader.load("config/towers.json", TowersConfig.class);
		return config;
	}

}
