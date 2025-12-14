package com.towerdefense.viewer.renderers;

import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.viewer.Renderer;

public class ProjectileRenderer implements Renderer<Projectile> {

    @Override
    public String[][] render(Projectile t) {
        return new String[][]{
            {" ▪️ " },
            {"D" + pad(t.damage(), 3) },
            {"..."}
        };
    }

    private String pad(Object n, int len) {
        return String.format("%" + len + "s", n);
    }
}
