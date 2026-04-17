package com.towerdefense.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.towerdefense.exception.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour renvoyer des réponses HTTP propres.
 * 
 * Utilise les exceptions métier pour générer des réponses HTTP appropriées
 * avec des codes d'erreur structurés.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions d'or insuffisant (400 Bad Request).
     */
    @ExceptionHandler(InsufficientGoldException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientGold(InsufficientGoldException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        error.addDetail("required", ex.getRequired());
        error.addDetail("available", ex.getAvailable());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    /**
     * Gère les exceptions de position occupée (409 Conflict).
     */
    @ExceptionHandler(PositionOccupiedException.class)
    public ResponseEntity<ErrorResponse> handlePositionOccupied(PositionOccupiedException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        error.addDetail("x", ex.getX());
        error.addDetail("y", ex.getY());
        error.addDetail("existingTowerId", ex.getTowerId());
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    /**
     * Gère les exceptions de tour non trouvée (404 Not Found).
     */
    @ExceptionHandler(TowerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTowerNotFound(TowerNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        error.addDetail("towerId", ex.getTowerId());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    /**
     * Gère les exceptions d'entité non trouvée (404 Not Found).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        error.addDetail("entityType", ex.getEntityType());
        error.addDetail("entityId", ex.getEntityId());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    /**
     * Gère les exceptions de tour non améliorable (409 Conflict).
     */
    @ExceptionHandler(TowerCannotBeUpgradedException.class)
    public ResponseEntity<ErrorResponse> handleTowerCannotUpgrade(TowerCannotBeUpgradedException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        error.addDetail("towerId", ex.getTowerId());
        error.addDetail("reason", ex.getReason());
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    /**
     * Gère les exceptions de tour non vendable (409 Conflict).
     */
    @ExceptionHandler(TowerCannotBeSoldException.class)
    public ResponseEntity<ErrorResponse> handleTowerCannotSell(TowerCannotBeSoldException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        error.addDetail("towerId", ex.getTowerId());
        error.addDetail("reason", ex.getReason());
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    /**
     * Gère les exceptions de niveau max atteint (409 Conflict).
     */
    @ExceptionHandler(TowerMaxRankReachedException.class)
    public ResponseEntity<ErrorResponse> handleTowerMaxLevel(TowerMaxRankReachedException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        error.addDetail("towerId", ex.getTowerId());
        error.addDetail("currentLevel", ex.getCurrentRank());
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    /**
     * Gère les exceptions de jeu terminé (409 Conflict).
     */
    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<ErrorResponse> handleGameOver(GameOverException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    /**
     * Gère les exceptions de jeu non initialisé (412 Precondition Failed).
     */
    @ExceptionHandler(GameNotInitializedException.class)
    public ResponseEntity<ErrorResponse> handleGameNotInitialized(GameNotInitializedException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.PRECONDITION_FAILED.value()
        );
        
        return ResponseEntity
            .status(HttpStatus.PRECONDITION_FAILED)
            .body(error);
    }

    /**
     * Gère les exceptions de configuration invalide (400 Bad Request).
     */
    @ExceptionHandler(InvalidConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidConfiguration(InvalidConfigurationException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        error.addDetail("configType", ex.getConfigType());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    /**
     * Gère les exceptions de type ennemi inconnu (400 Bad Request).
     */
    @ExceptionHandler(UnknownEnemyTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnknownEnemyType(UnknownEnemyTypeException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        error.addDetail("enemyTypeId", ex.getEnemyTypeId());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    /**
     * Gère les exceptions de type de tour inconnu (400 Bad Request).
     */
    @ExceptionHandler(UnknownTowerTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnknownTowerType(UnknownTowerTypeException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        error.addDetail("towerTypeId", ex.getTowerTypeId());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    /**
     * Gère les exceptions de chemin inconnu (400 Bad Request).
     */
    @ExceptionHandler(UnknownPathException.class)
    public ResponseEntity<ErrorResponse> handleUnknownPath(UnknownPathException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        error.addDetail("pathId", ex.getPathId());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    /**
     * Gère les exceptions d'action non autorisée (403 Forbidden).
     */
    @ExceptionHandler(UnauthorizedPlayerActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAction(UnauthorizedPlayerActionException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value()
        );
        error.addDetail("playerId", ex.getPlayerId());
        error.addDetail("action", ex.getAction());
        
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error);
    }

    /**
     * Gère les IllegalArgumentException génériques (400 Bad Request).
     * À utiliser en dernier recours pour les validations qui n'ont pas
     * encore d'exception métier dédiée.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
            "INVALID_ARGUMENT",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    /**
     * Gère les IllegalStateException génériques (409 Conflict).
     * À utiliser en dernier recours pour les états invalides qui n'ont pas
     * encore d'exception métier dédiée.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        ErrorResponse error = new ErrorResponse(
            "INVALID_STATE",
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère toutes les exceptions non gérées (500 Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }

    /**
     * Structure standardisée pour les réponses d'erreur.
     */
    public static class ErrorResponse {
        private String errorCode;
        private String message;
        private int status;
        private LocalDateTime timestamp;
        private Map<String, Object> details;

        public ErrorResponse(String errorCode, String message, int status) {
            this.errorCode = errorCode;
            this.message = message;
            this.status = status;
            this.timestamp = LocalDateTime.now();
            this.details = new HashMap<>();
        }

        public void addDetail(String key, Object value) {
            this.details.put(key, value);
        }

        // Getters pour la sérialisation JSON
        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public int getStatus() { return status; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getDetails() { return details; }
    }
}