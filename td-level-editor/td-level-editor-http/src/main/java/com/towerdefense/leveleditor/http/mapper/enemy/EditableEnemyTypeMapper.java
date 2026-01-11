package com.towerdefense.leveleditor.http.mapper.enemy;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;

/**
 * Mapper MapStruct pour la conversion entre :
 * - le modèle HTTP OpenAPI (leveleditor.http.model)
 * - le modèle domaine draft (editor.api.model.draft)
 *
 * Aucune logique métier ne doit apparaître ici.
 */
@Mapper(componentModel = "spring")
public interface EditableEnemyTypeMapper {

    /* ============================
     * HTTP -> Domaine
     * ============================ */

	EditableEnemyType toDomain(com.towerdefense.leveleditor.http.model.EditableEnemyType httpPath);

    /* ============================
     * Domaine -> HTTP
     * ============================ */

    com.towerdefense.leveleditor.http.model.EditableEnemyType toHttp(EditableEnemyType domainPosition);
}
