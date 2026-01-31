package com.towerdefense.progression.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration SIMPLIFIÉE pour le rechargement à chaud.
 * 
 * STRATÉGIE: Au lieu d'utiliser des proxies, on injecte directement
 * le ReloadableBeansManager dans les contrôleurs/services et on appelle
 * manager.getMetaGameService(), manager.getUpgradeRegistry(), etc.
 * 
 * Cette approche est plus simple et plus claire.
 */
@Configuration
@EnableScheduling
public class ProgressionConfiguration {
    
    /**
     * Le gestionnaire de beans rechargeables.
     * C'est le SEUL bean que vous devez injecter dans vos contrôleurs.
     */
    @Bean(initMethod = "initializeBeans")
    public ReloadableBeansManager reloadableBeansManager() {
        return new ReloadableBeansManager();
    }
    
    /**
     * Le service de rechargement à chaud.
     */
    @Bean
    public HotReloadService hotReloadService() {
        return new HotReloadService();
    }
}
