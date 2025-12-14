package com.towerdefense.viewer.renderers;

import com.towerdefense.domain.enemy.Enemy;
import com.towerdefense.viewer.Renderer;

public class EnemyRenderer implements Renderer<Enemy> {

    @Override
    public String[][] render(Enemy t) {
        if (t.health().isDead())
        	return new String[][]{
                { "..." },
                { "..." },
                { "..." }
            };
    	return new String[][]{
            { "  👾 " },
            { "S" + pad(t.speed(), 4) },
            { "H" + pad(t.health().current(), 4) }
        };
    }

    private String pad(Object n, int len) {
        return String.format("%" + len + "s", n);
    }
}
