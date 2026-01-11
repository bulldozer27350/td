package com.towerdefense.leveleditor.http.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.leveleditor.http.api.EnemiesApi;
import com.towerdefense.leveleditor.http.mapper.enemy.EditableEnemyTypeMapper;
import com.towerdefense.leveleditor.http.model.CreateEnemyTypeRequest;
import com.towerdefense.leveleditor.http.model.EditableEnemyType;
import com.towerdefense.leveleditor.http.model.ExportRequest;
import com.towerdefense.leveleditor.http.model.ImportRequest;

import jakarta.validation.Valid;

@RestController
public class EnemiesApiImpl extends AbstractEditorController implements EnemiesApi {

    private final EditableEnemyTypeMapper mapper;

    public EnemiesApiImpl(EditableEnemyTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<EditableEnemyType>> getAllEnemyTypes() {
        return ResponseEntity.ok(
            editorApi().getAllEditableEnemyTypes()
                .stream()
                .map(mapper::toHttp)
                .toList()
        );
    }

    @Override
    public ResponseEntity<Void> createEnemyType(@Valid CreateEnemyTypeRequest createEnemyTypeRequest) {
        boolean success = editorApi().createEnemyType(
            createEnemyTypeRequest.getId(),
            createEnemyTypeRequest.getHealth(),
            createEnemyTypeRequest.getSpeed(),
            createEnemyTypeRequest.getReward()
        );
        
        return success 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> deleteEnemyType(String enemyTypeId) {
        boolean success = editorApi().removeEnemyType(enemyTypeId);
        
        return success 
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> exportDraftEnemyType(String enemyTypeId, @Valid ExportRequest exportRequest) {
        boolean success = editorApi().saveDraftEnemyType(enemyTypeId, exportRequest.getPath());
        
        return success 
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> exportExportableEnemyType(String enemyTypeId, @Valid ExportRequest exportRequest) {
        boolean success = editorApi().saveExportableEnemyType(enemyTypeId, exportRequest.getPath());
        
        return success 
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<EditableEnemyType> getEnemyType(String enemyTypeId) {
        com.towerdefense.editor.api.model.draft.EditableEnemyType enemy = 
            editorApi().getEditableEnemyType(enemyTypeId);
        
        return enemy != null 
            ? ResponseEntity.ok(mapper.toHttp(enemy))
            : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> importDraftEnemyType(@Valid ImportRequest importRequest) {
        boolean success = editorApi().loadDraftEnemyType(importRequest.getPath());
        
        return success 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> importExportableEnemyType(@Valid ImportRequest importRequest) {
        boolean success = editorApi().loadExportableEnemyType(importRequest.getPath());
        
        return success 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}