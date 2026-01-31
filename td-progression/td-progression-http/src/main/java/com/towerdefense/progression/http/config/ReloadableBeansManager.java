package com.towerdefense.progression.http.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.towerdefense.progression.domain.upgrade.UpgradeRegistry;
import com.towerdefense.progression.service.MetaGameService;
import com.towerdefense.progression.service.UpgradeAdminService;

/**
 * Gestionnaire des beans qui peuvent être rechargés à chaud.
 * Permet de recréer les instances quand les fichiers sources changent.
 */
@Component
public class ReloadableBeansManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ReloadableBeansManager.class);
    
    @Value("${tdmeta.data.directory:src/main/resources/exportables}")
    private String dataDirectory;

    @Value("${tdmeta.upgrades.file:src/main/resources/upgrades/tower-upgrades.json}")
    private String upgradesFile;
    
    // Instances actuelles des beans
    private volatile UpgradeRegistry upgradeRegistry;
    private volatile MetaGameService metaGameService;
    private volatile UpgradeAdminService upgradeAdminService;
    
    // Flag pour éviter les rechargements concurrents
    private volatile boolean isReloading = false;
    
    /**
     * Initialise les beans au démarrage.
     */
    public synchronized void initializeBeans() throws Exception {
        logger.info("🚀 Initialisation des beans rechargeables...");
        
        // Créer UpgradeRegistry
        this.upgradeRegistry = new UpgradeRegistry(Path.of(upgradesFile));
        logger.info("✅ UpgradeRegistry créé");
        
        // Créer MetaGameService
        this.metaGameService = new MetaGameService(Path.of(dataDirectory), upgradeRegistry);
        logger.info("✅ MetaGameService créé");
        
        // Créer UpgradeAdminService
        this.upgradeAdminService = new UpgradeAdminService(Path.of(upgradesFile), upgradeRegistry);
        logger.info("✅ UpgradeAdminService créé");
        
        logger.info("🎉 Tous les beans sont initialisés");
    }
    
    /**
     * Recharge tous les beans.
     */
    public synchronized void reloadBeans() {
        if (isReloading) {
            logger.warn("⚠️ Rechargement déjà en cours, ignoré");
            return;
        }
        
        isReloading = true;
        
        try {
            logger.info("🔄 Début du rechargement des beans...");
            
            // Sauvegarder les anciennes instances
            UpgradeRegistry oldRegistry = this.upgradeRegistry;
            MetaGameService oldMetaGameService = this.metaGameService;
            UpgradeAdminService oldAdminService = this.upgradeAdminService;
            
            try {
                // Créer les nouvelles instances
                UpgradeRegistry newRegistry = new UpgradeRegistry(Path.of(upgradesFile));
                MetaGameService newMetaGameService = new MetaGameService(Path.of(dataDirectory), newRegistry);
                UpgradeAdminService newAdminService = new UpgradeAdminService(Path.of(upgradesFile), newRegistry);
                
                // Remplacer atomiquement les instances
                this.upgradeRegistry = newRegistry;
                this.metaGameService = newMetaGameService;
                this.upgradeAdminService = newAdminService;
                
                logger.info("✅ Beans rechargés avec succès");
                
                // Optionnel : cleanup des anciennes instances si nécessaire
                // oldRegistry.cleanup();
                // oldMetaGameService.cleanup();
                
            } catch (Exception e) {
                // En cas d'erreur, restaurer les anciennes instances
                logger.error("❌ Erreur lors du rechargement, restauration des anciennes instances", e);
                this.upgradeRegistry = oldRegistry;
                this.metaGameService = oldMetaGameService;
                this.upgradeAdminService = oldAdminService;
                throw e;
            }
            
        } catch (Exception e) {
            logger.error("❌ Échec du rechargement des beans", e);
        } finally {
            isReloading = false;
        }
    }
    
    /**
     * Retourne la liste des chemins à surveiller.
     */
    public List<Path> getWatchedPaths() {
        List<Path> paths = new ArrayList<>();
        paths.add(Path.of(dataDirectory));
        paths.add(Path.of(upgradesFile));
        return paths;
    }
    
    // Getters pour accéder aux instances actuelles
    public UpgradeRegistry getUpgradeRegistry() {
        return upgradeRegistry;
    }
    
    public MetaGameService getMetaGameService() {
        return metaGameService;
    }
    
    public UpgradeAdminService getUpgradeAdminService() {
        return upgradeAdminService;
    }
}