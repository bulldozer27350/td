package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

public class EditableAttack {

    private final List<EditableWave> waves = new ArrayList<>();
    private String id;
    
    // For serialization
    private EditableAttack() {
    	
    }
    
    public EditableAttack(String id) {
    	this.id = id;
	}

    public List<EditableWave> getWaves() {
        return waves;
    }

    public EditableAttack addWave(EditableWave wave) {
        waves.add(wave);
        return this;
    }
    
    public String getId() {
		return id;
	}
}
