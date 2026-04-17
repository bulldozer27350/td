package com.towerdefense.progression.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.towerdefense.progression.domain.upgrade.ModifierType;
import com.towerdefense.progression.domain.upgrade.TowerUpgrade;
import com.towerdefense.progression.domain.upgrade.UpgradeEffect;
import com.towerdefense.progression.model.EffectDefinition;
import com.towerdefense.progression.model.UpgradeDefinition;
import com.towerdefense.progression.model.UpgradeDefinitions;

public class UpgradePersistenceService {
    
    private static final Logger log = LoggerFactory.getLogger(UpgradePersistenceService.class);
    
    private final Path upgradesJsonPath;
    private final ObjectMapper objectMapper;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public UpgradePersistenceService(Path upgradesJsonPath) {
        this.upgradesJsonPath = upgradesJsonPath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    /**
     * Charge les upgrades depuis le fichier JSON de manière Thread-Safe.
     */
    public UpgradeDefinitions loadUpgrades() throws IOException {
        lock.readLock().lock();
        try {
            if (!Files.exists(upgradesJsonPath)) {
                // Relâcher le lock en lecture, prendre le lock en écriture pour initialiser
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    if (!Files.exists(upgradesJsonPath)) {
                        UpgradeDefinitions empty = new UpgradeDefinitions(List.of());
                        saveUpgradesInternal(empty);
                        return empty;
                    }
                } finally {
                    // Reprendre le lock en lecture avant de relâcher l'écriture
                    lock.readLock().lock();
                    lock.writeLock().unlock();
                }
            }
            return objectMapper.readValue(upgradesJsonPath.toFile(), UpgradeDefinitions.class);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Sauvegarde les upgrades dans le fichier JSON de manière Thread-Safe.
     */
    public void saveUpgrades(UpgradeDefinitions definitions) throws IOException {
        lock.writeLock().lock();
        try {
            saveUpgradesInternal(definitions);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private void saveUpgradesInternal(UpgradeDefinitions definitions) throws IOException {
        Files.createDirectories(upgradesJsonPath.getParent());
        objectMapper.writeValue(upgradesJsonPath.toFile(), definitions);
        log.info("[PERSISTENCE] Saved {} upgrades to {}", definitions.upgrades().size(), upgradesJsonPath);
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
