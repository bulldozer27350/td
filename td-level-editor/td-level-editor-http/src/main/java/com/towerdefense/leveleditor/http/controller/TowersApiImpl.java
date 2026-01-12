package com.towerdefense.leveleditor.http.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.leveleditor.http.api.TowersApi;
import com.towerdefense.leveleditor.http.mapper.tower.EditableTowerTypeMapper;
import com.towerdefense.leveleditor.http.model.CreateTowerTypeRequest;
import com.towerdefense.leveleditor.http.model.EditableTowerType;
import com.towerdefense.leveleditor.http.model.ExportRequest;
import com.towerdefense.leveleditor.http.model.ImportRequest;

import jakarta.validation.Valid;

@RestController
public class TowersApiImpl extends AbstractEditorController implements TowersApi {

    private final EditableTowerTypeMapper mapper;

    public TowersApiImpl(EditableTowerTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> createTowerType(@Valid CreateTowerTypeRequest createTowerTypeRequest) {
        TowerTypeEditorApi towerEditor = editorApi().createTowerType(
            createTowerTypeRequest.getId(),
            createTowerTypeRequest.getName()
        );
        
        return towerEditor != null 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> deleteTowerType(String towerTypeId) {
        boolean success = editorApi().removeTowerType(towerTypeId);
        
        return success 
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> exportDraftTowerType(String towerTypeId, @Valid ExportRequest exportRequest) {
        boolean success = editorApi().saveDraftTowerType(towerTypeId, exportRequest.getPath());
        
        return success 
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> exportExportableTowerType(String towerTypeId, @Valid ExportRequest exportRequest) {
        boolean success = editorApi().saveExportableTowerType(towerTypeId, exportRequest.getPath());
        
        return success 
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<List<EditableTowerType>> getAllTowerTypes() {
        List<EditableTowerType> list = editorApi().getAllEditableTowerTypes()
		    .stream()
		    .map(mapper::toHttp)
		    .toList();
		return ResponseEntity.ok(
            list
        );
    }

    @Override
    public ResponseEntity<EditableTowerType> getTowerType(String towerTypeId) {
        TowerTypeEditorApi towerEditor = editorApi().getTowerTypeEditor(towerTypeId);
        
        if (towerEditor != null) {
            EditableTowerType httpTower = mapper.toHttp(towerEditor.getCurrentTowerType());
            return ResponseEntity.ok(httpTower);
        }
        
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> importDraftTowerType(@Valid ImportRequest importRequest) {
        TowerTypeEditorApi towerEditor = editorApi().loadDraftTowerType(importRequest.getPath());
        
        return towerEditor != null 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> importExportableTowerType(@Valid ImportRequest importRequest) {
        TowerTypeEditorApi towerEditor = editorApi().loadExportableTowerType(importRequest.getPath());
        
        return towerEditor != null 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}