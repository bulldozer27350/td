package com.towerdefense.leveleditor.http.mapper.level;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.towerdefense.editor.api.model.draft.EditableAttack;

@Mapper(
    componentModel = "spring",
    uses = {
        EditableWaveMapper.class
    }
)
public interface EditableAttackMapper {

    EditableAttackMapper INSTANCE = Mappers.getMapper(EditableAttackMapper.class);

    /* ============================================================
     * Domaine -> HTTP
     * ============================================================ */

    com.towerdefense.leveleditor.http.model.EditableAttack toHttp(EditableAttack domain);

    /* ============================================================
     * HTTP -> Domaine
     * ============================================================ */

    EditableAttack toDomain(com.towerdefense.leveleditor.http.model.EditableAttack http);
}

