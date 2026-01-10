package com.towerdefense.exception;

/**
 * Exception levée quand une entité est introuvable.
 */
public class EntityNotFoundException extends TowerDefenseException {
    private final String entityId;
    private final String entityType;
    
    public EntityNotFoundException(String entityType, String entityId) {
        super(
            "ENTITY_NOT_FOUND",
            String.format("%s with id %s not found", entityType, entityId)
        );
        this.entityId = entityId;
        this.entityType = entityType;
    }
    
    public String getEntityId() { return entityId; }
    public String getEntityType() { return entityType; }
}