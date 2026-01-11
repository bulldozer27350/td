package com.towerdefense.leveleditor.http.mapper.level;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableMap;

@Mapper(componentModel = "spring", uses = EditablePathMapper.class)
public interface EditableMapMapper {

    com.towerdefense.leveleditor.http.model.EditableMap toHttp(EditableMap domain);

    EditableMap toDomain(com.towerdefense.leveleditor.http.model.EditableMap http);
}
