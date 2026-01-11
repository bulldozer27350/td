package com.towerdefense.leveleditor.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.leveleditor.http.api.TowerEditingApi;
import com.towerdefense.leveleditor.http.mapper.tower.EditableTowerLevelMapper;
import com.towerdefense.leveleditor.http.model.EditableTowerLevel;

import jakarta.validation.Valid;

@RestController
public class TowerEditingApiImpl extends AbstractEditorController implements TowerEditingApi {

    private final EditableTowerLevelMapper mapper;

    public TowerEditingApiImpl(EditableTowerLevelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> addTowerLevel(String towerTypeId, @Valid EditableTowerLevel editableTowerLevel) {
        TowerTypeEditorApi towerEditor = editorApi().getTowerTypeEditor(towerTypeId);
        
        if (towerEditor == null) {
            return ResponseEntity.notFound().build();
        }

        com.towerdefense.editor.api.model.draft.EditableTowerLevel domainLevel = 
            mapper.toDomain(editableTowerLevel);
        
        boolean success = towerEditor.addTowerLevel(
            domainLevel.level(),
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
    public ResponseEntity<Void> removeTowerLevel(String towerTypeId, Integer level) {
        TowerTypeEditorApi towerEditor = editorApi().getTowerTypeEditor(towerTypeId);
        
        if (towerEditor == null) {
            return ResponseEntity.notFound().build();
        }

        boolean success = towerEditor.removeTowerLevel(level);
        
        return success 
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}