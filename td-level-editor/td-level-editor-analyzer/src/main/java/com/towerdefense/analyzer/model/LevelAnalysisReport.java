package com.towerdefense.analyzer.model;

import java.util.ArrayList;
import java.util.List;

public class LevelAnalysisReport {
    
    private final int difficultyScore; // 0-100
    private final List<AnalysisWarning> warnings = new ArrayList<>();
    private final List<AnalysisMetric> metrics = new ArrayList<>();
    
    public LevelAnalysisReport(int difficultyScore) {
        this.difficultyScore = Math.max(0, Math.min(100, difficultyScore));
    }
    
    public void addWarning(String category, String message, Severity severity) {
        warnings.add(new AnalysisWarning(category, message, severity));
    }
    
    public void addMetric(String name, double value, String unit) {
        metrics.add(new AnalysisMetric(name, value, unit));
    }
    
    public int getDifficultyScore() { return difficultyScore; }
    public List<AnalysisWarning> getWarnings() { return warnings; }
    public List<AnalysisMetric> getMetrics() { return metrics; }
    
    public enum Severity { INFO, WARNING, CRITICAL }
    
    public static class AnalysisWarning {
        private final String category;
        private final String message;
        private final Severity severity;
        
        public AnalysisWarning(String category, String message, Severity severity) {
            this.category = category;
            this.message = message;
            this.severity = severity;
        }
        
        public String getCategory() { return category; }
        public String getMessage() { return message; }
        public Severity getSeverity() { return severity; }
    }
    
    public static class AnalysisMetric {
        private final String name;
        private final double value;
        private final String unit;
        
        public AnalysisMetric(String name, double value, String unit) {
            this.name = name;
            this.value = value;
            this.unit = unit;
        }
        
        public String getName() { return name; }
        public double getValue() { return value; }
        public String getUnit() { return unit; }
    }
}