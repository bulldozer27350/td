package com.towerdefense.leveleditor.http.mapper.tower;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableTowerType;

@Mapper(
    componentModel = "spring",
    uses = EditableTowerLevelMapper.class
)
public interface EditableTowerTypeMapper {

    EditableTowerType toDomain(com.towerdefense.leveleditor.http.model.EditableTowerType http);

    com.towerdefense.leveleditor.http.model.EditableTowerType toHttp(EditableTowerType domain);
}
