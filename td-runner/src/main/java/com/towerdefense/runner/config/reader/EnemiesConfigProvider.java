package com.towerdefense.runner.config.reader;

import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

import java.io.File;

public class EnemiesConfigProvider implements DTOProvider<EnemiesConfig> {

	@Override
	public EnemiesConfig providesDTO() {
		JsonConfigLoader loader = new JsonConfigLoader();
		EnemiesConfig config = loader.load(String.join(File.separator,"config","enemies.json"), EnemiesConfig.class);
		return config;
	}

}
