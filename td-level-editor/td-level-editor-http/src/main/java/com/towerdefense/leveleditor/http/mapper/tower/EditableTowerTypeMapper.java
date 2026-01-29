package com.towerdefense.leveleditor.http.mapper.tower;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableTowerRank;
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
        
        if (domain.ranks() != null) {
            List<com.towerdefense.leveleditor.http.model.EditableTowerRank> httpLevels = 
                new ArrayList<>();
            
            for (EditableTowerRank domainLevel : domain.ranks()) {
                httpLevels.add(towerLevelToHttp(domainLevel));
            }
            
            http.setRanks(httpLevels);
        }
        
        return http;
    }

    default EditableTowerType toDomain(com.towerdefense.leveleditor.http.model.EditableTowerType http) {
        if (http == null) {
            return null;
        }
        
        EditableTowerType domain = new EditableTowerType(http.getId(), http.getTowerType());
        
        if (http.getRanks() != null) {
            for (com.towerdefense.leveleditor.http.model.EditableTowerRank httpLevel : http.getRanks()) {
                domain.addRank(towerRankToDomain(httpLevel));
            }
        }
        
        return domain;
    }
    
    // Helper methods pour mapper les levels
    default com.towerdefense.leveleditor.http.model.EditableTowerRank towerLevelToHttp(EditableTowerRank domain) {
        if (domain == null) {
            return null;
        }
        
        com.towerdefense.leveleditor.http.model.EditableTowerRank http = 
            new com.towerdefense.leveleditor.http.model.EditableTowerRank();
        
        http.setRank(domain.rank());
        http.setCost(domain.cost());
        http.setRange(domain.range());
        http.setDamage(domain.damage());
        http.setReloadTime(domain.reloadTime());
        http.setSellReward(domain.sellReward());
        http.setBuildTimeTicks(domain.buildTimeTicks());
        
        return http;
    }
    
    default EditableTowerRank towerRankToDomain(com.towerdefense.leveleditor.http.model.EditableTowerRank http) {
        if (http == null) {
            return null;
        }
        
        return new EditableTowerRank(
            http.getRank(),
            http.getCost(),
            http.getRange(),
            http.getDamage(),
            http.getReloadTime(),
            http.getSellReward(),
            http.getBuildTimeTicks()
        );
    }
}