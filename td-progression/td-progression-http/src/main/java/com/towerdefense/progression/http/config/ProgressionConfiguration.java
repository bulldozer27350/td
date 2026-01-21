package com.towerdefense.progression.http.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.progression.domain.upgrade.UpgradeRegistry;
import com.towerdefense.progression.service.MetaGameService;
import com.towerdefense.progression.service.UpgradeAdminService;

@Configuration
public class ProgressionConfiguration {
    
//	@Value("D:\\Depots\\tower_defense\\exportables\\")
	@Value("${tdmeta.data.directory:src/main/resources/exportables}")
    private String dataDirectory;
    
    @Value("${tdmeta.upgrades.file:src/main/resources/upgrades/tower-upgrades.json}")
    private String upgradesFile;
    
    @Bean
    public UpgradeRegistry upgradeRegistry() throws Exception {
        return new UpgradeRegistry(Path.of(upgradesFile));
    }
    
    @Bean
    public MetaGameService metaGameService(UpgradeRegistry upgradeRegistry) throws Exception {
        return new MetaGameService(Path.of(dataDirectory), upgradeRegistry);
    }
    
    @Bean
    public UpgradeAdminService upgradeAdminService(UpgradeRegistry upgradeRegistry) {
        return new UpgradeAdminService(Path.of(upgradesFile), upgradeRegistry);
    }
}
