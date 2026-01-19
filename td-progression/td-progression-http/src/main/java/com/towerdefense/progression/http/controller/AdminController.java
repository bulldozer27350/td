package com.towerdefense.progression.http.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.progression.http.api.AdminApi;
import com.towerdefense.progression.http.model.CreateUpgradeRequest;
import com.towerdefense.progression.http.model.ReloadUpgrades200Response;
import com.towerdefense.progression.http.model.UpdateUpgradeRequest;
import com.towerdefense.progression.http.model.UpgradeDefinition;
import com.towerdefense.progression.http.model.UpgradeEffect;
import com.towerdefense.progression.http.model.UpgradeEffect.TypeEnum;
import com.towerdefense.progression.model.EffectDefinition;
import com.towerdefense.progression.service.UpgradeAdminService;

import jakarta.validation.Valid;

@RestController
public class AdminController implements AdminApi {

    private final UpgradeAdminService upgradeAdminService;
    
    public AdminController(UpgradeAdminService upgradeAdminService) {
        this.upgradeAdminService = upgradeAdminService;
    }
    
    @Override
    public ResponseEntity<List<com.towerdefense.progression.http.model.UpgradeDefinition>> listAllUpgrades() {
        List<com.towerdefense.progression.model.UpgradeDefinition> definitions = 
            upgradeAdminService.getAllUpgradeDefinitions();
        
        List<UpgradeDefinition> dtos = definitions.stream()
            .map(this::toHttpEntity)
            .toList();
        
        return ResponseEntity.ok(dtos);
    }
    
    @Override
    public ResponseEntity<UpgradeDefinition> createUpgrade(@Valid CreateUpgradeRequest request) {
        try {
            // Convertir la requête HTTP en modèle domain
            EffectDefinition effect = new EffectDefinition(
                request.getEffect().getStat(),
                request.getEffect().getModifier(),
                request.getEffect().getType().name()
            );
            
            com.towerdefense.progression.model.UpgradeDefinition newUpgrade = 
                new com.towerdefense.progression.model.UpgradeDefinition(
                    request.getId(),
                    request.getName(),
                    request.getTowerTypeId(),
                    request.getTowerLevel(),
                    request.getCost(),
                    effect
                );
            
            // Créer l'upgrade
            com.towerdefense.progression.model.UpgradeDefinition created = 
                upgradeAdminService.createUpgrade(newUpgrade);
            
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toHttpEntity(created));
                
        } catch (IllegalArgumentException e) {
            // Upgrade existe déjà
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IOException e) {
            System.err.println("[ADMIN] Error creating upgrade: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Override
    public ResponseEntity<UpgradeDefinition> updateUpgrade(
    		@PathVariable("upgradeId") String upgradeId,
            @Valid UpdateUpgradeRequest request) {
        
        try {
            // Convertir l'effet si fourni
            EffectDefinition effect = null;
            if (request.getEffect() != null) {
                effect = new EffectDefinition(
                    request.getEffect().getStat(),
                    request.getEffect().getModifier(),
                    request.getEffect().getType().name()
                );
            }
            
            // Mettre à jour
            var updated = upgradeAdminService.updateUpgrade(
                upgradeId,
                request.getName(),
                request.getCost(),
                effect
            );
            
            if (updated.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(toHttpEntity(updated.get()));
            
        } catch (IOException e) {
            System.err.println("[ADMIN] Error updating upgrade: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Override
    public ResponseEntity<Void> deleteUpgrade(@PathVariable("upgradeId") String upgradeId) {
        try {
            boolean deleted = upgradeAdminService.deleteUpgrade(upgradeId);
            
            if (!deleted) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.noContent().build();
            
        } catch (IOException e) {
            System.err.println("[ADMIN] Error deleting upgrade: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Override
    public ResponseEntity<ReloadUpgrades200Response> reloadUpgrades() {
        try {
            int count = upgradeAdminService.reloadUpgrades();
            
            ReloadUpgrades200Response response = new ReloadUpgrades200Response();
            response.setCount(count);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            System.err.println("[ADMIN] Error reloading upgrades: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Convertit un UpgradeDefinition domain en DTO HTTP
     */
    private UpgradeDefinition toHttpEntity(com.towerdefense.progression.model.UpgradeDefinition def) {
        UpgradeEffect effect = new UpgradeEffect();
        effect.setModifier(def.effect().modifier());
        effect.setStat(def.effect().stat());
        effect.setType(TypeEnum.valueOf(def.effect().type()));
        
        UpgradeDefinition upgrade = new UpgradeDefinition();
        upgrade.setId(def.id());
        upgrade.setEffect(effect);
        upgrade.setCost(def.cost());
        upgrade.setName(def.name());
        upgrade.setTowerLevel(def.towerLevel());
        upgrade.setTowerTypeId(def.towerTypeId());
        
        return upgrade;
    }
}