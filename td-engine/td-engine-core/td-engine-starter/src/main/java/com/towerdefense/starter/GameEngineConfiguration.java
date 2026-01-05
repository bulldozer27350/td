package com.towerdefense.starter;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.EngineContext;
import com.towerdefense.config.assembler.EnemyFactoryAssembler;
import com.towerdefense.config.assembler.LevelAssembler;
import com.towerdefense.config.assembler.PathAssembler;
import com.towerdefense.config.assembler.TowerTypeAssembler;
import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Configuration
public class GameEngineConfiguration {

	@Bean
	GameEngineApi getGameEngineApi(LevelScenarioFactory levelScenarioFactory, 
			EngineContext context, 
			TowerServices towerServices, 
			TowerTypeAssembler towerTypeAssembler,
			EnemyFactoryAssembler enemyFactoryAssembler, 
			LevelAssembler levelAssembler,
			PathAssembler pathAssembler, Map<Class<?>, GameCommandHandler<?>> gameCommandHandlers) {
		return new GameEngineApiImpl(levelScenarioFactory, context,
				towerTypeAssembler, enemyFactoryAssembler, levelAssembler, pathAssembler, gameCommandHandlers);
	}
	
}
