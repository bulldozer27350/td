package com.towerdefense.engine.tests.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.towerdefense.engine.api.model.configuration.GameConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enregistre les configurations qui ont causé des échecs de tests
 * pour permettre le rejeu et le debugging.
 */
public class TestFailureRecorder {

    private static final String FAILURES_DIR = "target/test-failures";
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Enregistre une configuration qui a causé un échec de test.
     * 
     * @param testName nom du test qui a échoué
     * @param config configuration problématique
     * @param error erreur capturée
     * @return chemin du fichier créé
     */
    public static Path recordFailure(String testName, GameConfig config, Throwable error) {
        try {
            // Crée le répertoire s'il n'existe pas
            Path failuresDir = Paths.get(FAILURES_DIR);
            Files.createDirectories(failuresDir);

            // Génère un nom de fichier unique avec timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = String.format("%s_%s.json", 
                    sanitizeTestName(testName), 
                    timestamp);
            Path filePath = failuresDir.resolve(fileName);

            // Crée un wrapper avec métadonnées
            FailureRecord record = new FailureRecord(
                    testName,
                    timestamp,
                    error.getClass().getName(),
                    error.getMessage(),
                    getStackTraceString(error),
                    config
            );

            // Sauvegarde en JSON
            MAPPER.writeValue(filePath.toFile(), record);

            System.err.println("❌ ÉCHEC ENREGISTRÉ : " + filePath);
            System.err.println("   Pour rejouer : TestFailureReplayer.replay(\"" + fileName + "\")");

            return filePath;

        } catch (IOException e) {
            System.err.println("⚠️  Impossible d'enregistrer l'échec : " + e.getMessage());
            return null;
        }
    }

    /**
     * Charge une configuration depuis un fichier d'échec.
     */
    public static GameConfig loadFailure(String fileName) throws IOException {
        Path filePath = Paths.get(FAILURES_DIR, fileName);
        FailureRecord record = MAPPER.readValue(filePath.toFile(), FailureRecord.class);
        return record.config;
    }

    /**
     * Liste tous les fichiers d'échecs enregistrés.
     */
    public static void listFailures() throws IOException {
        Path failuresDir = Paths.get(FAILURES_DIR);
        if (!Files.exists(failuresDir)) {
            System.out.println("Aucun échec enregistré.");
            return;
        }

        System.out.println("📋 Échecs enregistrés :");
        Files.list(failuresDir)
                .filter(p -> p.toString().endsWith(".json"))
                .forEach(p -> {
                    try {
                        FailureRecord record = MAPPER.readValue(
                                p.toFile(), 
                                FailureRecord.class
                        );
                        System.out.printf("  - %s [%s] : %s%n",
                                p.getFileName(),
                                record.timestamp,
                                record.errorMessage);
                    } catch (IOException e) {
                        System.out.println("  - " + p.getFileName() + " (erreur de lecture)");
                    }
                });
    }

    /**
     * Nettoie tous les fichiers d'échecs (utile pour CI/CD).
     */
    public static void cleanFailures() throws IOException {
        Path failuresDir = Paths.get(FAILURES_DIR);
        if (Files.exists(failuresDir)) {
            Files.walk(failuresDir)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            // Ignore
                        }
                    });
            System.out.println("✅ Échecs nettoyés");
        }
    }

    // ========================
    // MÉTHODES UTILITAIRES
    // ========================

    private static String sanitizeTestName(String testName) {
        return testName.replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    private static String getStackTraceString(Throwable error) {
        StringBuilder sb = new StringBuilder();
        sb.append(error.toString()).append("\n");
        for (StackTraceElement element : error.getStackTrace()) {
            sb.append("    at ").append(element.toString()).append("\n");
            if (sb.length() > 2000) { // Limite pour ne pas surcharger le JSON
                sb.append("    ... (truncated)\n");
                break;
            }
        }
        return sb.toString();
    }

    // ========================
    // INNER CLASS
    // ========================

    /**
     * Record enregistrant un échec avec ses métadonnées.
     */
    public static class FailureRecord {
        public String testName;
        public String timestamp;
        public String errorType;
        public String errorMessage;
        public String stackTrace;
        public GameConfig config;

        // Constructeur pour Jackson
        public FailureRecord() {}

        public FailureRecord(String testName, String timestamp, 
                             String errorType, String errorMessage, 
                             String stackTrace, GameConfig config) {
            this.testName = testName;
            this.timestamp = timestamp;
            this.errorType = errorType;
            this.errorMessage = errorMessage;
            this.stackTrace = stackTrace;
            this.config = config;
        }
    }
}