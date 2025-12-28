package com.towerdefense.editor.api.model;

import java.util.List;

public record EditablePath(String id, List<PositionDefinition> points) {
}
