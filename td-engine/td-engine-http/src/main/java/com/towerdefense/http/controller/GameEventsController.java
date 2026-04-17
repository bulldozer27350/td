package com.towerdefense.http.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.events.EnemyHitEvent;
import com.towerdefense.engine.api.model.events.EnemyKilledEvent;
import com.towerdefense.engine.api.model.events.EnemyMovedEvent;
import com.towerdefense.engine.api.model.events.TowerPlacedEvent;
import com.towerdefense.engine.api.model.events.TowerShotEvent;
import com.towerdefense.engine.api.model.events.TowerSoldEvent;
import com.towerdefense.engine.api.model.events.TowerUpgradedEvent;

import com.towerdefense.http.session.GameSessionManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Game Events", description = "Server-Sent Events for real-time game updates")
public class GameEventsController {

    private static final Logger log = LoggerFactory.getLogger(GameEventsController.class);
    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    
    @GetMapping(value = "/game/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(
        summary = "Stream game events via SSE",
        description = "Opens a persistent connection to receive real-time game events: tower-shot, enemy-hit, enemy-killed, state-update, game-won, game-lost"
    )
    @ApiResponse(responseCode = "200", description = "Event stream opened")
    public SseEmitter streamEvents(@RequestParam("clientId") String clientId) {
        SseEmitter existing = emitters.remove(clientId);
        if (existing != null) {
            existing.complete();
            log.debug("Existing SSE replaced for clientId={}", clientId);
        }

        SseEmitter emitter = new SseEmitter(300_000L);
        
        emitter.onCompletion(() -> {
            log.debug("SSE connection completed - removing emitter for clientId={}", clientId);
            emitters.remove(clientId);
        });
        
        emitter.onTimeout(() -> {
            log.debug("SSE connection timeout - removing emitter for clientId={}", clientId);
            emitters.remove(clientId);
        });
        
        emitter.onError((e) -> {
            log.error("SSE connection error - removing emitter for clientId={}: {}", clientId, e.getMessage());
            emitters.remove(clientId);
        });
        
        emitters.put(clientId, emitter);

        log.info("New SSE connection established ({}). Total emitters: {}", clientId, emitters.size());
        return emitter;
    }
    
    public void sendEventToClient(String clientId, String eventType, Object data) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventType).data(data));
            } catch (Exception e) {
                log.error("Removing dead SSE emitter for clientId={}: {}", clientId, e.getMessage());
                emitters.remove(clientId);
                emitter.complete();
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    public void heartbeat() {
        emitters.forEach((clientId, emitter) -> {
            sendEventToClient(clientId, "ping", "ping");
        });
    }

    /**
     * Crée un observateur dédié pour un client spécifique, résolvant le problème de broadcast global.
     */
    public GameStateObserver forClient(String clientId, GameSessionManager sessionManager) {
        return new GameStateObserver() {
            @Override
            public void onEnemyHit(EnemyHitEvent event) { sendEventToClient(clientId, "enemy-hit", event); }
            @Override
            public void onEnemyKilled(EnemyKilledEvent event) { sendEventToClient(clientId, "enemy-killed", event); }
            @Override
            public void onEnemyMoved(EnemyMovedEvent event) { sendEventToClient(clientId, "enemy-moved", event); }
            @Override
            public void onTowerPlaced(TowerPlacedEvent event) { sendEventToClient(clientId, "tower-placed", event); }
            @Override
            public void onTowerShot(TowerShotEvent event) { sendEventToClient(clientId, "tower-shot", event); }
            @Override
            public void onTowerUpgraded(TowerUpgradedEvent event) { sendEventToClient(clientId, "tower-upgraded", event); }
            @Override
            public void onTowerSold(TowerSoldEvent event) { sendEventToClient(clientId, "tower-sold", event); }
            @Override
            public void onStateUpdated(GameStateDTO state, int tick) {
                if (tick % 5 == 0) {
                    sendEventToClient(clientId, "state-update", state);
                }
            }
            @Override
            public void onGameCreated(LevelMapDTO levelMap) { sendEventToClient(clientId, "game-created", levelMap); }
            @Override
            public void onGameWon(GameStateDTO state) {
                sendEventToClient(clientId, "game-won", state);
                SseEmitter em = emitters.remove(clientId);
                if (em != null) em.complete();
                sessionManager.removeSession(clientId); // Libère la RAM côté serveur
            }
            @Override
            public void onGameLoose(GameStateDTO state) {
                sendEventToClient(clientId, "game-lost", state);
                SseEmitter em = emitters.remove(clientId);
                if (em != null) em.complete();
                sessionManager.removeSession(clientId); // Libère la RAM côté serveur
            }
        };
    }
}