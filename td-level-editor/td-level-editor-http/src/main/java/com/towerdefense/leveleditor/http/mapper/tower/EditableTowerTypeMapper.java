package com.towerdefense.leveleditor.http.mapper.tower;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableTowerLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;

@Mapper(componentModel = "spring")
public interface EditableTowerTypeMapper {

    default com.towerdefense.leveleditor.http.model.EditableTowerType toHttp(EditableTowerType domain) {
        if (domain == null) {
            return null;
        }
        
        com.towerdefense.leveleditor.http.model.EditableTowerType http = 
            new com.towerdefense.leveleditor.http.model.EditableTowerType();
        
        http.setId(domain.id());
        http.setTowerType(domain.towerType());
        
        if (domain.upgrades() != null) {
            List<com.towerdefense.leveleditor.http.model.EditableTowerLevel> httpLevels = 
                new ArrayList<>();
            
            for (EditableTowerLevel domainLevel : domain.upgrades()) {
                httpLevels.add(towerLevelToHttp(domainLevel));
            }
            
            http.setUpgrades(httpLevels);
        }
        
        return http;
    }

    default EditableTowerType toDomain(com.towerdefense.leveleditor.http.model.EditableTowerType http) {
        if (http == null) {
            return null;
        }
        
        EditableTowerType domain = new EditableTowerType(http.getId(), http.getTowerType());
        
        if (http.getUpgrades() != null) {
            for (com.towerdefense.leveleditor.http.model.EditableTowerLevel httpLevel : http.getUpgrades()) {
                domain.addUpgrade(towerLevelToDomain(httpLevel));
            }
        }
        
        return domain;
    }
    
    // Helper methods pour mapper les levels
    default com.towerdefense.leveleditor.http.model.EditableTowerLevel towerLevelToHttp(EditableTowerLevel domain) {
        if (domain == null) {
            return null;
        }
        
        com.towerdefense.leveleditor.http.model.EditableTowerLevel http = 
            new com.towerdefense.leveleditor.http.model.EditableTowerLevel();
        
        http.setLevel(domain.level());
        http.setCost(domain.cost());
        http.setRange(domain.range());
        http.setDamage(domain.damage());
        http.setReloadTime(domain.reloadTime());
        http.setSellReward(domain.sellReward());
        http.setBuildTimeTicks(domain.buildTimeTicks());
        
        return http;
    }
    
    default EditableTowerLevel towerLevelToDomain(com.towerdefense.leveleditor.http.model.EditableTowerLevel http) {
        if (http == null) {
            return null;
        }
        
        return new EditableTowerLevel(
            http.getLevel(),
            http.getCost(),
            http.getRange(),
            http.getDamage(),
            http.getReloadTime(),
            http.getSellReward(),
            http.getBuildTimeTicks()
        );
    }
}