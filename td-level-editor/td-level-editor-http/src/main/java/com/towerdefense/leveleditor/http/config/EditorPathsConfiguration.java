package com.towerdefense.leveleditor.http.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "editor.storage")
public class EditorPathsConfiguration {
    
    private String baseDirectory = "D:\\Depots\\tower_defense";
    
    // Sous-répertoires
    private String draftsDir = "drafts";
    private String exportablesDir = "exportables";
    
    // Types d'entités
    private String towersDir = "towers";
    private String enemiesDir = "enemies";
    private String levelsDir = "levels";
    
    // Getters et setters
    public String getBaseDirectory() { return baseDirectory; }
    public void setBaseDirectory(String baseDirectory) { this.baseDirectory = baseDirectory; }
    
    public String getDraftsDir() { return draftsDir; }
    public void setDraftsDir(String draftsDir) { this.draftsDir = draftsDir; }
    
    public String getExportablesDir() { return exportablesDir; }
    public void setExportablesDir(String exportablesDir) { this.exportablesDir = exportablesDir; }
    
    public String getTowersDir() { return towersDir; }
    public void setTowersDir(String towersDir) { this.towersDir = towersDir; }
    
    public String getEnemiesDir() { return enemiesDir; }
    public void setEnemiesDir(String enemiesDir) { this.enemiesDir = enemiesDir; }
    
    public String getLevelsDir() { return levelsDir; }
    public void setLevelsDir(String levelsDir) { this.levelsDir = levelsDir; }
    
    // Méthodes utilitaires
    public String getDraftTowersPath() {
        return String.join("\\", baseDirectory, draftsDir, towersDir);
    }
    
    public String getDraftEnemiesPath() {
        return String.join("\\", baseDirectory, draftsDir, enemiesDir);
    }
    
    public String getDraftLevelsPath() {
        return String.join("\\", baseDirectory, draftsDir, levelsDir);
    }
    
    public String getExportableTowersPath() {
        return String.join("\\", baseDirectory, exportablesDir, towersDir);
    }
    
    public String getExportableEnemiesPath() {
        return String.join("\\", baseDirectory, exportablesDir, enemiesDir);
    }
    
    public String getExportableLevelsPath() {
        return String.join("\\", baseDirectory, exportablesDir, levelsDir);
    }
}