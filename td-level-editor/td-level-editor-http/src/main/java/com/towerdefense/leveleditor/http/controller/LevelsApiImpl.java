package com.towerdefense.leveleditor.http.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.leveleditor.http.api.LevelsApi;
import com.towerdefense.leveleditor.http.mapper.level.EditableLevelMapper;
import com.towerdefense.leveleditor.http.model.CreateLevelRequest;
import com.towerdefense.leveleditor.http.model.EditableLevel;
import com.towerdefense.leveleditor.http.model.ExportRequest;
import com.towerdefense.leveleditor.http.model.ImportRequest;

import jakarta.validation.Valid;

@RestController
public class LevelsApiImpl extends AbstractEditorController implements LevelsApi {

    private final EditableLevelMapper levelMapper;

    public LevelsApiImpl(EditableLevelMapper levelMapper) {
        this.levelMapper = levelMapper;
    }

    @Override
    public ResponseEntity<EditableLevel> createLevel(@Valid CreateLevelRequest createLevelRequest) {
        LevelEditorApi levelEditor = editorApi().createLevel(
            createLevelRequest.getId(),
            createLevelRequest.getWidth(),
            createLevelRequest.getHeight()
        );
        
        if (levelEditor != null) {
            EditableLevel httpLevel = levelMapper.toHttp(levelEditor.getCurrentLevel());
            return ResponseEntity.status(HttpStatus.CREATED).body(httpLevel);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> deleteLevel(String levelId) {
        boolean success = editorApi().removeLevel(levelId);
        
        return success 
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> exportDraftLevel(String levelId, @Valid ExportRequest exportRequest) {
        boolean success = editorApi().saveDraftLevel(levelId, exportRequest.getPath());
        
        return success 
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> exportExportableLevel(String levelId, @Valid ExportRequest exportRequest) {
        boolean success = editorApi().saveExportableLevel(levelId, exportRequest.getPath());
        
        return success 
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<List<EditableLevel>> getAllLevels() {
        return ResponseEntity.ok(
            editorApi().getAllEditableLevels()
                .stream()
                .map(levelMapper::toHttp)
                .toList()
        );
    }

    @Override
    public ResponseEntity<EditableLevel> getLevel(String levelId) {
        com.towerdefense.editor.api.model.draft.EditableLevel level = 
            editorApi().getEditableLevel(levelId);
        
        return level != null 
            ? ResponseEntity.ok(levelMapper.toHttp(level))
            : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<EditableLevel> importDraftLevel(@Valid ImportRequest importRequest) {
        LevelEditorApi levelEditor = editorApi().loadDraftLevel(importRequest.getPath());
        
        if (levelEditor != null) {
            EditableLevel httpLevel = levelMapper.toHttp(levelEditor.getCurrentLevel());
            return ResponseEntity.status(HttpStatus.CREATED).body(httpLevel);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> importExportableLevel(@Valid ImportRequest importRequest) {
        LevelEditorApi levelEditor = editorApi().loadExportableLevel(importRequest.getPath());
        
        return levelEditor != null 
            ? ResponseEntity.status(HttpStatus.CREATED).build()
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}