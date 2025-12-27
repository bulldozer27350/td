package com.towerdefense.engine.api.model;

import java.util.List;

public record LevelMapDTO(MapDimensionsDTO dimensions, List<WayDTO> paths) {

}
