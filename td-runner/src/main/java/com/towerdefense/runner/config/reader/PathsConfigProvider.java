package com.towerdefense.runner.config.reader;

import com.towerdefense.engine.api.model.configuration.PathsConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

import java.io.File;

public class PathsConfigProvider implements DTOProvider<PathsConfig> {

	@Override
	public PathsConfig providesDTO() {
		JsonConfigLoader loader = new JsonConfigLoader();
        PathsConfig config =
            loader.load(String.join(File.separator,"config","paths.json"), PathsConfig.class);
        return config;

	}

}
