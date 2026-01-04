package com.towerdefense.editor.api.model.draft;

import java.util.List;

import com.towerdefense.editor.api.model.exportable.PositionDefinition;

public record EditablePath(String id, List<PositionDefinition> points) {
}
