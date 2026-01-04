package com.towerdefense.leveleditor.ui.render;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;

public final class AsciiEnemyTypeTableRenderer {

    public String render(List<EditableEnemyType> enemies) {
        StringBuilder sb = new StringBuilder();

        sb.append("=============== ENEMY TYPES ===============\n\n");
        sb.append(String.format("%-10s | %-4s | %-5s | %-6s\n",
                "ID", "HP", "SPEED", "REWARD"));
        sb.append("-----------+------+-------+--------\n");

        for (EditableEnemyType e : enemies) {
            sb.append(String.format("%-10s | %-4d | %-5.2f | %-6d\n",
                    e.getId(),
                    e.getHealth(),
                    e.getSpeed(),
                    e.getReward()));
        }

        return sb.toString();
    }
}

