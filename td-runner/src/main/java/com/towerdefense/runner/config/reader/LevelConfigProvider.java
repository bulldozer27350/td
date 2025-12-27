package com.towerdefense.runner.config.reader;

import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

public class LevelConfigProvider implements DTOProvider<LevelConfig> {

	@Override
	public LevelConfig providesDTO() {
		JsonConfigLoader loader = new JsonConfigLoader();
		LevelConfig config = loader.load("config/levels/level-1.json", LevelConfig.class);
		return config;
	}
}
