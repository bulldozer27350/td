package com.towerdefense.editor.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.editor.api.TowerDefenseEditorApi;

@Configuration
public class EditorConfiguration {
    
    @Bean
    public TowerDefenseEditorApi towerDefenseEditorApi() {
        return new EditorContext().getTowerDefenseApi();
    }
}
