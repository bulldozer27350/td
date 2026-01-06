package com.towerdefense.services.internal.impl.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.services.GameCommandHandler;

@Configuration
public class GameCommandConfiguration {

	@Bean
    public Map<Class<?>, GameCommandHandler<?>> gameCommandHandlers(
            List<GameCommandHandler<?>> handlers) {

        Map<Class<?>, GameCommandHandler<?>> map = new HashMap<>();

        for (GameCommandHandler<?> handler : handlers) {
            map.put(handler.commandType(), handler);
        }

        return map;
    }

}
