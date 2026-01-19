package com.towerdefense.leveleditor.http.mapper.level;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.draft.EditableLevel;

/**
 * Mapper MapStruct pour la conversion entre : - le modèle HTTP OpenAPI
 * (leveleditor.http.model) - le modèle domaine draft (editor.api.model.draft)
 *
 * Aucune logique métier ne doit apparaître ici.
 */
@Mapper(componentModel = "spring", uses = { 
		EditablePathMapper.class, 
		EditableAttackMapper.class,
		EditableMapMapper.class })
public interface EditableLevelMapper {

	/*
	 * ============================================================ Domaine -> HTTP
	 * ============================================================
	 */

	com.towerdefense.leveleditor.http.model.EditableLevel toHttp(EditableLevel domain);

	/*
	 * ============================================================ HTTP -> Domaine
	 * ============================================================
	 */

	EditableLevel toDomain(com.towerdefense.leveleditor.http.model.EditableLevel http);
}
