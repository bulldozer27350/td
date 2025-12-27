package com.towerdefense.viewer.renderers;

import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.engine.api.model.TowerStateEnum;
import com.towerdefense.viewer.Renderer;

public class TowerRenderer implements Renderer<TowerDTO> {

    @Override
    public String[][] render(TowerDTO t) {
        if (t.state() == TowerStateEnum.BUILDING || t.state() == TowerStateEnum.UPDATING) {
        	return new String[][]{
        		{ "  🏗️ " },
        		{ "..." },
        		{ "..."  }
        	};
        } else if (t.state() == TowerStateEnum.RELOADING) {
        	return new String[][]{
        		{ "  🕑 " },
        		{ "..." },
        		{ "..."  }
        	};
        }
        return new String[][]{
            { "  🗼 " },
            { "..." },
            { "..." }
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
