package com.towerdefense.runner.config;

import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Configuration class for the sequencer component of the tower defense game.
 */
public class SequencerConfiguration {

	@Bean
	/**
	 * Bean definition for LevelScenarioFactory.
	 */
	public LevelScenarioFactory levelScenarioFactory() {
		return new LevelScenarioFactory();
	}
}
