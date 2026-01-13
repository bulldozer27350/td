package com.towerdefense.leveleditor.http.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.analyzer.LevelAnalyzer;
import com.towerdefense.analyzer.model.LevelAnalysisReport;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.leveleditor.http.api.LevelAnalysisApi;

@RestController
public class LevelAnalysisApiImpl extends AbstractEditorController implements LevelAnalysisApi {

    @Override
    public ResponseEntity<com.towerdefense.leveleditor.http.model.LevelAnalysisReport> analyzeLevel(String levelId) {
        EditableLevel level = editorApi().getEditableLevel(levelId);
        
        if (level == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Créer l'analyseur avec tous les types disponibles
        LevelAnalyzer analyzer = new LevelAnalyzer(
            editorApi().getAllEditableEnemyTypes(),
            editorApi().getAllEditableTowerTypes()
        );
        
        // Analyser
        LevelAnalysisReport domainReport = analyzer.analyze(level);
        
        // Mapper vers HTTP (vous devrez créer un mapper)
        com.towerdefense.leveleditor.http.model.LevelAnalysisReport httpReport = 
            mapToHttp(domainReport);
        
        return ResponseEntity.ok(httpReport);
    }
    
    private com.towerdefense.leveleditor.http.model.LevelAnalysisReport mapToHttp(
            LevelAnalysisReport domainReport) {
        
        com.towerdefense.leveleditor.http.model.LevelAnalysisReport http = 
            new com.towerdefense.leveleditor.http.model.LevelAnalysisReport();
        
        http.setDifficultyScore(domainReport.getDifficultyScore());
        
        // Mapper les warnings
        for (LevelAnalysisReport.AnalysisWarning warning : domainReport.getWarnings()) {
            com.towerdefense.leveleditor.http.model.AnalysisWarning httpWarning = 
                new com.towerdefense.leveleditor.http.model.AnalysisWarning();
            httpWarning.setCategory(warning.getCategory());
            httpWarning.setMessage(warning.getMessage());
            httpWarning.setSeverity(
                com.towerdefense.leveleditor.http.model.AnalysisWarning.SeverityEnum.valueOf(
                    warning.getSeverity().name()
                )
            );
            http.addWarningsItem(httpWarning);
        }
        
        // Mapper les métriques
        for (LevelAnalysisReport.AnalysisMetric metric : domainReport.getMetrics()) {
            com.towerdefense.leveleditor.http.model.AnalysisMetric httpMetric = 
                new com.towerdefense.leveleditor.http.model.AnalysisMetric();
            httpMetric.setName(metric.getName());
            httpMetric.setValue(metric.getValue());
            httpMetric.setUnit(metric.getUnit());
            http.addMetricsItem(httpMetric);
        }
        
        return http;
    }
}