package com.towerdefense.leveleditor.http.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.towerdefense.editor.api.TowerDefenseEditorApi;

public abstract class AbstractEditorController {

	@Autowired
    private TowerDefenseEditorApi towerDefenseApi;
    
    protected TowerDefenseEditorApi editorApi() {
        return towerDefenseApi;
    }
}
