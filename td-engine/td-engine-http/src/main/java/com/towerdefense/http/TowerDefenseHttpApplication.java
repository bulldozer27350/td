package com.towerdefense.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.towerdefense.http.controller")
public class TowerDefenseHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(TowerDefenseHttpApplication.class, args);
    }
}