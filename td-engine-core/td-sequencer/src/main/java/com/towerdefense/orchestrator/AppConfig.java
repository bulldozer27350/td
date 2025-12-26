package com.towerdefense.orchestrator;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.towerdefense")
/** 
 * Configuration class for the Tower Defense application.
 */
public class AppConfig {
}
