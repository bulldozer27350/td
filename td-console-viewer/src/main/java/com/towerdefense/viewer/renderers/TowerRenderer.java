package com.towerdefense.viewer.renderers;

import com.towerdefense.domain.tower.Tower;
import com.towerdefense.viewer.Renderer;

public class TowerRenderer implements Renderer<Tower> {

    @Override
    public String[][] render(Tower t) {
        return new String[][]{
            { "  🗼 " },
            { "P " + pad(t.damage(), 3) },
            { "R" + pad(t.isReady(), 3) }
        };
    }

    private String pad(Object n, int len) {
        return String.format("%" + len + "s", n);
    }
    
    private String pad(boolean n, int len) {
    	if (n) {return String.format("%" + len + "s", "✔");}
    	else return String.format("%" + len + "s", "✖️");
    }
}
