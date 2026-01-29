package com.towerdefense.leveleditor.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.leveleditor.http.api.TowerEditingApi;
import com.towerdefense.leveleditor.http.mapper.tower.EditableTowerTypeMapper;
import com.towerdefense.leveleditor.http.model.EditableTowerRank;

import jakarta.validation.Valid;

@RestController
public class TowerEditingApiImpl extends AbstractEditorController implements TowerEditingApi {

	private final EditableTowerTypeMapper mapper;
	
    public TowerEditingApiImpl(EditableTowerTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> addTowerRank(String towerTypeId, @Valid EditableTowerRank editableTowerRank) {
        TowerTypeEditorApi towerEditor = editorApi().getTowerTypeEditor(towerTypeId);
        
        if (towerEditor == null) {
            return ResponseEntity.notFound().build();
        }

        com.towerdefense.editor.api.model.draft.EditableTowerRank domainLevel = 
            mapper.towerRankToDomain(editableTowerRank);
        
        boolean success = towerEditor.addTowerRank(
            domainLevel.rank(),
            domainLevel.damage(),
            domainLevel.reloadTime(),
            domainLevel.cost(),
            domainLevel.range(),
            domainLevel.sellReward(),
            domainLevel.buildTimeTicks()
        );
        
        return success 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> removeTowerRank(String towerTypeId, Integer rank) {
        TowerTypeEditorApi towerEditor = editorApi().getTowerTypeEditor(towerTypeId);
        
        if (towerEditor == null) {
            return ResponseEntity.notFound().build();
        }

        boolean success = towerEditor.removeTowerRank(rank);
        
        return success 
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}