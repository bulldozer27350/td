package com.towerdefense.leveleditor.http.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.towerdefense.leveleditor", 
		"com.towerdefense.leveleditor.http.bootstrap", 
		"com.towerdefense.editor.core.configuration"})
public class TowerDefenseStudioHttp {
	public static void main(String[] args) {
        SpringApplication.run(TowerDefenseStudioHttp.class, args);
    }
}
