package com.towerdefense.leveleditor.ui.render;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerRank;
import com.towerdefense.editor.api.model.draft.EditableTowerType;

public final class AsciiTowerTypeRenderer {

    public String render(List<EditableTowerType> towers) {
        StringBuilder sb = new StringBuilder();
        sb.append("=============== TOWER TYPES ===============\n\n");

        for (EditableTowerType tower : towers) {
            sb.append("Tower: ").append(tower.id()).append("\n");
            sb.append(String.format("%-3s | %-4s | %-4s | %-6s | %-5s | %-6s | %-4s\n",
                    "Lvl", "Cost", "Sell", "Damage", "Range", "B Time", "Rate"));
            sb.append("----+------+------+--------+-------+--------+-----\n");

            for (EditableTowerRank level : tower.ranks()) {
                sb.append(String.format("%-3d | %-4d | %-4d | %-6d | %-3.3f | %-6d | %-4.2f\n",
                        level.rank(),
                        level.cost(),
                        level.sellReward(),
                        level.damage(),
                        level.range(),
                        level.buildTimeTicks(),
                        level.reloadTime()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

