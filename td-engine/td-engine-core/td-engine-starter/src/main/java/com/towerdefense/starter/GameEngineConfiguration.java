package com.towerdefense.starter;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.EngineContext;
import com.towerdefense.config.assembler.EnemyFactoryAssembler;
import com.towerdefense.config.assembler.LevelAssembler;
import com.towerdefense.config.assembler.TowerTypeAssembler;
import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Configuration
public class GameEngineConfiguration {

	@Bean
	public LevelScenarioFactory levelScenarioFactory() {
		return new LevelScenarioFactory();
	}

	@Bean
	GameEngineApi getGameEngineApi(LevelScenarioFactory levelScenarioFactory, EngineContext context,
			TowerServices towerServices, TowerTypeAssembler towerTypeAssembler,
			EnemyFactoryAssembler enemyFactoryAssembler, LevelAssembler levelAssembler,
			Map<Class<?>, GameCommandHandler<?>> gameCommandHandlers) {
		return new GameEngineApiImpl(levelScenarioFactory, context, towerTypeAssembler, enemyFactoryAssembler,
				levelAssembler, gameCommandHandlers);
	}

	@Bean
	GameRuntime getGameRuntime(GameEngineApi gameEngineApi) {
		return new GameRuntimeImpl(gameEngineApi);
	}

}
