package com.towerdefense.runner.config.reader;

import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

import java.io.File;

public class LevelConfigProvider implements DTOProvider<LevelConfig> {

	@Override
	public LevelConfig providesDTO() {
		JsonConfigLoader loader = new JsonConfigLoader();
		LevelConfig config = loader.load(String.join(File.separator,"config","levels","rank-1.json"), LevelConfig.class);
		return config;
	}
}
