package com.towerdefense.runner.config;

import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SequencerConfiguration {

    @Bean
    public LevelScenarioFactory levelScenarioFactory() {
        return new LevelScenarioFactory();
    }
}

