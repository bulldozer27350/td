package com.towerdefense.viewer.renderers;

import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.viewer.Renderer;

public class ProjectileRenderer implements Renderer<ProjectileDTO> {

    @Override
    public String[][] render(ProjectileDTO t) {
        return new String[][]{
            { " ▪️ " },
            { "..." },
            { "..." }
        };
    }

    private String pad(Object n, int len) {
        return String.format("%" + len + "s", n);
    }
}
