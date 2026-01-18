package com.towerdefense.progression.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.towerdefense.progression.domain.upgrade.UpgradeRegistry;
import com.towerdefense.progression.model.EffectDefinition;
import com.towerdefense.progression.model.UpgradeDefinition;
import com.towerdefense.progression.model.UpgradeDefinitions;

/**
 * Service pour gérer les opérations CRUD sur les upgrades avec persistence
 */
public class UpgradeAdminService {
    
    private final UpgradePersistenceService persistenceService;
    private final UpgradeRegistry upgradeRegistry;
    
    public UpgradeAdminService(Path upgradesJsonPath, UpgradeRegistry upgradeRegistry) {
        this.persistenceService = new UpgradePersistenceService(upgradesJsonPath);
        this.upgradeRegistry = upgradeRegistry;
    }
    
    /**
     * Récupère tous les upgrades
     */
    public List<UpgradeDefinition> getAllUpgradeDefinitions() {
        return upgradeRegistry.getAllUpgrades().stream()
            .map(persistenceService::toDefinition)
            .toList();
    }
    
    /**
     * Crée un nouvel upgrade
     */
    public UpgradeDefinition createUpgrade(UpgradeDefinition newUpgrade) throws IOException {
        // Vérifier que l'ID n'existe pas déjà
        if (upgradeRegistry.getUpgrade(newUpgrade.id()) != null) {
            throw new IllegalArgumentException("Upgrade with id " + newUpgrade.id() + " already exists");
        }
        
        // Charger les upgrades existants
        UpgradeDefinitions current = persistenceService.loadUpgrades();
        
        // Ajouter le nouvel upgrade
        List<UpgradeDefinition> updated = new ArrayList<>(current.upgrades());
        updated.add(newUpgrade);
        
        // Sauvegarder
        UpgradeDefinitions newDefinitions = new UpgradeDefinitions(updated);
        persistenceService.saveUpgrades(newDefinitions);
        
        // Recharger le registry
        reloadRegistry();
        
        return newUpgrade;
    }
    
    /**
     * Met à jour un upgrade existant
     */
    public Optional<UpgradeDefinition> updateUpgrade(String upgradeId, 
                                                     String newName, 
                                                     Integer newCost, 
                                                     EffectDefinition newEffect) throws IOException {
        // Charger les upgrades existants
        UpgradeDefinitions current = persistenceService.loadUpgrades();
        
        // Trouver et mettre à jour l'upgrade
        List<UpgradeDefinition> updated = new ArrayList<>();
        UpgradeDefinition updatedUpgrade = null;
        boolean found = false;
        
        for (UpgradeDefinition def : current.upgrades()) {
            if (def.id().equals(upgradeId)) {
                found = true;
                // Appliquer les modifications (garder les valeurs existantes si null)
                String finalName = newName != null ? newName : def.name();
                int finalCost = newCost != null ? newCost : def.cost();
                EffectDefinition finalEffect = newEffect != null ? newEffect : def.effect();
                
                updatedUpgrade = new UpgradeDefinition(
                    def.id(),
                    finalName,
                    def.towerTypeId(),
                    def.towerLevel(),
                    finalCost,
                    finalEffect
                );
                updated.add(updatedUpgrade);
            } else {
                updated.add(def);
            }
        }
        
        if (!found) {
            return Optional.empty();
        }
        
        // Sauvegarder
        UpgradeDefinitions newDefinitions = new UpgradeDefinitions(updated);
        persistenceService.saveUpgrades(newDefinitions);
        
        // Recharger le registry
        reloadRegistry();
        
        return Optional.of(updatedUpgrade);
    }
    
    /**
     * Supprime un upgrade
     */
    public boolean deleteUpgrade(String upgradeId) throws IOException {
        // Charger les upgrades existants
        UpgradeDefinitions current = persistenceService.loadUpgrades();
        
        // Filtrer l'upgrade à supprimer
        List<UpgradeDefinition> updated = current.upgrades().stream()
            .filter(def -> !def.id().equals(upgradeId))
            .toList();
        
        // Vérifier qu'un upgrade a été supprimé
        if (updated.size() == current.upgrades().size()) {
            return false; // Aucun upgrade trouvé
        }
        
        // Sauvegarder
        UpgradeDefinitions newDefinitions = new UpgradeDefinitions(updated);
        persistenceService.saveUpgrades(newDefinitions);
        
        // Recharger le registry
        reloadRegistry();
        
        return true;
    }
    
    /**
     * Recharge les upgrades depuis le fichier JSON
     */
    public int reloadUpgrades() throws IOException {
        UpgradeDefinitions definitions = persistenceService.loadUpgrades();
        reloadRegistry();
        return definitions.upgrades().size();
    }
    
    /**
     * Recharge le registry avec les données du fichier
     * Note: Cette méthode nécessite de recréer le registry
     */
    private void reloadRegistry() throws IOException {
        // Le registry doit être rechargé par l'application
        // On pourrait implémenter une méthode reload() dans UpgradeRegistry
        System.out.println("[ADMIN] Registry reload needed - application restart recommended");
        upgradeRegistry.reload();
    }
}
