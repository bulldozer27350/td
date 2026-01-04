package com.towerdefense.leveleditor.ui.bootstrap;

import com.towerdefense.leveleditor.ui.cli.TowerDefenseStudioCli;
import com.towerdefense.leveleditor.ui.context.EditorContext;

public class EditorApplication {

    public static void main(String[] args) {

        EditorContext context = new EditorContext();

        TowerDefenseStudioCli cli = new TowerDefenseStudioCli();
        cli.start(context);
    }
}
