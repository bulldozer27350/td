package com.towerdefense.viewer;

import java.util.HashMap;
import java.util.Map;

public class RendererRegistry {

    private final Map<Class<?>, Renderer<?>> registry = new HashMap<>();

    public <T> void register(Class<T> cls, Renderer<T> renderer) {
        registry.put(cls, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T> Renderer<T> getRenderer(T obj) {
        if (obj == null) return null;
        return (Renderer<T>) registry.get(obj.getClass());
    }
}
