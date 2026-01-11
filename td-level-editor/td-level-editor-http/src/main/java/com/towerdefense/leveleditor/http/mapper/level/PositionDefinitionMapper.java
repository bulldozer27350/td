package com.towerdefense.leveleditor.http.mapper.level;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.exportable.PositionDefinition;

@Mapper(componentModel = "spring")
public interface PositionDefinitionMapper {

    PositionDefinition toDomain(com.towerdefense.leveleditor.http.model.PositionDefinition http);

    com.towerdefense.leveleditor.http.model.PositionDefinition toHttp(PositionDefinition domain);
}
