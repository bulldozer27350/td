package com.towerdefense.leveleditor.http.mapper.level;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditablePath;

/**
 * Mapper MapStruct pour la conversion entre :
 * - le modèle HTTP OpenAPI (leveleditor.http.model)
 * - le modèle domaine draft (editor.api.model.draft)
 *
 * Aucune logique métier ne doit apparaître ici.
 */
@Mapper(componentModel = "spring", uses = PositionDefinitionMapper.class)
public interface EditablePathMapper {

    /* ============================
     * HTTP -> Domaine
     * ============================ */

    EditablePath toDomain(com.towerdefense.leveleditor.http.model.EditablePath httpEditablePath);

    /* ============================
     * Domaine -> HTTP
     * ============================ */

    com.towerdefense.leveleditor.http.model.EditablePath toHttp(EditablePath domainEditablePath);
}
