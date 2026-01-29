package com.towerdefense.http.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.http.MediaType;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Game Events", description = "Server-Sent Events for real-time game updates")
public class GameEventsController implements GameStateObserver {

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    
    @GetMapping(value = "/game/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(
        summary = "Stream game events via SSE",
        description = "Opens a persistent connection to receive real-time game events: tower-shot, enemy-hit, enemy-killed, state-update, game-won, game-lost"
    )
    @ApiResponse(responseCode = "200", description = "Event stream opened")
    public SseEmitter streamEvents(@RequestParam("clientId") String clientId) {
     // Si un emitter existe déjà → on le ferme
        SseEmitter existing = emitters.remove(clientId);
        if (existing != null) {
            existing.complete();
            System.out.println("Existing SSE replaced for clientId=" + clientId);
        }

        SseEmitter emitter = new SseEmitter(300_000L);
        
        emitter.onCompletion(() -> {
            System.out.println("SSE connection completed - removing emitter");
            emitters.remove(clientId);
        });
        
        emitter.onTimeout(() -> {
            System.out.println("SSE connection timeout - removing emitter");
            emitters.remove(clientId);
        });
        
        emitter.onError((e) -> {
            System.out.println("SSE connection error - removing emitter: " + e.getMessage());
            emitters.remove(clientId);
        });
        
        emitters.put(clientId, emitter);

        System.out.println("SSE connected: " + clientId);
        System.out.println("New SSE connection established (" + clientId + "). Total emitters: " + emitters.size());
        return emitter;
    }
    
    private void sendEventToAll(String eventType, Object data) {
        System.out.println("Sending event to " + emitters.size() + " emitters: " + eventType);
        
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                    .name(eventType)
                    .data(data));
            } catch (IOException e) {
                emitters.remove(clientId);
            }
        });
        
        System.out.println("Active emitters after send: " + emitters.size());
    }


    @Override
    public void onEnemyHit(EnemyHitEvent event) {
        sendEventToAll("enemy-hit", event);
    }

    @Override
    public void onEnemyKilled(EnemyKilledEvent event) {
        sendEventToAll("enemy-killed", event);
    }
    
    @Override
    public void onEnemyMoved(EnemyMovedEvent event) {
        sendEventToAll("enemy-moved", event);
    }

    @Override
    public void onTowerPlaced(TowerPlacedEvent event) {
        sendEventToAll("tower-placed", event);
    }

    @Override
    public void onTowerShot(TowerShotEvent event) {
        sendEventToAll("tower-shot", event);
    }
    
    @Override
    public void onTowerUpgraded(TowerUpgradedEvent event) {
        sendEventToAll("tower-upgraded", event);
    }

    @Override
    public void onTowerSold(TowerSoldEvent event) {
        sendEventToAll("tower-sold", event);
    }

    @Override
    public void onStateUpdated(GameStateDTO state, int tick) {
        if (tick % 5 == 0) {
            sendEventToAll("state-update", state);
        }
    }

    @Override
    public void onGameCreated(LevelMapDTO levelMap) {
        sendEventToAll("game-created", levelMap);
    }

    @Override
    public void onGameWon(GameStateDTO state) {
        sendEventToAll("game-won", state);
    }

    @Override
    public void onGameLoose() {
        sendEventToAll("game-lost", null);
    }
}