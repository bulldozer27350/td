package com.towerdefense.progression.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.towerdefense.progression.domain.upgrade.ModifierType;
import com.towerdefense.progression.domain.upgrade.TowerUpgrade;
import com.towerdefense.progression.domain.upgrade.UpgradeEffect;
import com.towerdefense.progression.model.EffectDefinition;
import com.towerdefense.progression.model.UpgradeDefinition;
import com.towerdefense.progression.model.UpgradeDefinitions;

public class UpgradePersistenceService {
    
    private final Path upgradesJsonPath;
    private final ObjectMapper objectMapper;
    
    public UpgradePersistenceService(Path upgradesJsonPath) {
        this.upgradesJsonPath = upgradesJsonPath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    /**
     * Charge les upgrades depuis le fichier JSON
     */
    public UpgradeDefinitions loadUpgrades() throws IOException {
        if (!Files.exists(upgradesJsonPath)) {
            // Créer un fichier vide avec une liste vide si inexistant
            UpgradeDefinitions empty = new UpgradeDefinitions(List.of());
            saveUpgrades(empty);
            return empty;
        }
        return objectMapper.readValue(upgradesJsonPath.toFile(), UpgradeDefinitions.class);
    }
    
    /**
     * Sauvegarde les upgrades dans le fichier JSON
     */
    public void saveUpgrades(UpgradeDefinitions definitions) throws IOException {
        // Créer le répertoire parent si nécessaire
        Files.createDirectories(upgradesJsonPath.getParent());
        
        objectMapper.writeValue(upgradesJsonPath.toFile(), definitions);
        System.out.println("[PERSISTENCE] Saved " + definitions.upgrades().size() + 
                         " upgrades to " + upgradesJsonPath);
    }
    
    /**
     * Convertit un TowerUpgrade en UpgradeDefinition pour la persistence
     */
    public UpgradeDefinition toDefinition(TowerUpgrade upgrade) {
        EffectDefinition effect = new EffectDefinition(
            upgrade.getEffect().stat(),
            upgrade.getEffect().modifier(),
            upgrade.getEffect().type().name()
        );
        
        return new UpgradeDefinition(
            upgrade.getId(),
            upgrade.getName(),
            upgrade.getTowerTypeId(),
            upgrade.getTowerName(),
            upgrade.getTowerRank(),
            upgrade.getCost(),
            effect
        );
    }
    
    /**
     * Convertit un UpgradeDefinition en TowerUpgrade
     */
    public TowerUpgrade fromDefinition(UpgradeDefinition def) {
        ModifierType modifierType = ModifierType.valueOf(def.effect().type());
        UpgradeEffect effect = new UpgradeEffect(
            def.effect().stat(),
            def.effect().modifier(),
            modifierType
        );
        
        return new TowerUpgrade(
            def.id(),
            def.name(),
            def.towerTypeId(),
            def.towerName(),
            def.towerRank(),
            def.cost(),
            effect
        );
    }
}
