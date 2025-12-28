package com.towerdefense.editor.api.model;

import java.util.ArrayList;
import java.util.List;

public class EditableAttack {

    private final List<EditableWave> waves = new ArrayList<>();

    public List<EditableWave> getWaves() {
        return waves;
    }

    public EditableAttack addWave(EditableWave wave) {
        waves.add(wave);
        return this;
    }
}
