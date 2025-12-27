package com.towerdefense.viewer.renderers;

import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.viewer.Renderer;

public class EnemyRenderer implements Renderer<EnemyDTO> {

    @Override
    public String[][] render(EnemyDTO t) {
        if (!t.isAlive())
        	return new String[][]{
                { "..." },
                { "..." },
                { "..." }
            };
    	return new String[][]{
            { "  👾 " },
            { "M" + pad(t.maxHp(), 4) },
            { "H" + pad(t.currentHp(), 4) }
        };
    }

    private String pad(Object n, int len) {
        return String.format("%" + len + "s", n);
    }
}
