package com.towerdefense.runner.config.reader;

import com.towerdefense.engine.api.model.configuration.PathsConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

public class PathsConfigProvider implements DTOProvider<PathsConfig> {

	@Override
	public PathsConfig providesDTO() {
		JsonConfigLoader loader = new JsonConfigLoader();
        PathsConfig config =
            loader.load("config/paths.json", PathsConfig.class);
        return config;

	}

}
