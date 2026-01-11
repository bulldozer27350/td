package com.towerdefense.leveleditor.http.mapper.tower;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableTowerLevel;

@Mapper(componentModel = "spring")
public interface EditableTowerLevelMapper {

    EditableTowerLevel toDomain(com.towerdefense.leveleditor.http.model.EditableTowerLevel http);

    com.towerdefense.leveleditor.http.model.EditableTowerLevel toHttp(EditableTowerLevel domain);
}
