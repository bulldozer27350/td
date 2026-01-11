package com.towerdefense.leveleditor.http.mapper.level;


import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableWave;

@Mapper(componentModel = "spring")
public interface EditableWaveMapper {

    /* ============================================================
     * Domaine -> HTTP
     * ============================================================ */

    com.towerdefense.leveleditor.http.model.EditableWave toHttp(EditableWave domain);

    /* ============================================================
     * HTTP -> Domaine
     * ============================================================ */

    EditableWave toDomain(com.towerdefense.leveleditor.http.model.EditableWave http);
}
