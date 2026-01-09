package com.towerdefense.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.EngineContext;
import com.towerdefense.config.assembler.EnemyFactoryAssembler;
import com.towerdefense.config.assembler.LevelAssembler;
import com.towerdefense.config.assembler.PathAssembler;
import com.towerdefense.config.assembler.TowerTypeAssembler;

@Configuration
public class EngineInternalConfiguration {

	@Bean
	EngineContext engineContext() {
		return new EngineContext();
	}

	@Bean
	TowerTypeAssembler towerTypeConfiguration() {
		return new TowerTypeAssembler();
	}

	@Bean
	EnemyFactoryAssembler enemyFactoryConfiguration() {
		return new EnemyFactoryAssembler();
	}

	@Bean
	LevelAssembler levelConfiguration() {
		return new LevelAssembler();
	}
	
	@Bean
	PathAssembler pathConfiguration() {
		return new PathAssembler();
	}
}
