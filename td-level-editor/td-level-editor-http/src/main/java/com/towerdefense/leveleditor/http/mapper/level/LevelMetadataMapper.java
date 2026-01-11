package com.towerdefense.leveleditor.http.mapper.level;

import org.mapstruct.Mapper;

import com.towerdefense.editor.api.model.exportable.LevelMetadata;

/**
 * Mapper MapStruct pour la conversion entre :
 * - le modèle HTTP OpenAPI (leveleditor.http.model)
 * - le modèle domaine draft (editor.api.model.draft)
 *
 * Aucune logique métier ne doit apparaître ici.
 */
@Mapper(componentModel = "spring")
public interface LevelMetadataMapper {
	
	/* ============================
     * HTTP -> Domaine
     * ============================ */

	LevelMetadata toDomain(com.towerdefense.leveleditor.http.model.LevelMetadata httpLevelMetadata);

    /* ============================
     * Domaine -> HTTP
     * ============================ */

    com.towerdefense.leveleditor.http.model.LevelMetadata toHttp(LevelMetadata domainLevelMetadata);

}
