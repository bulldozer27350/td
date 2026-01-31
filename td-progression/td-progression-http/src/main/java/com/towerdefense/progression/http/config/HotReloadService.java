package com.towerdefense.progression.http.config;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service de rechargement à chaud des fichiers de configuration.
 * Surveille les modifications dans les répertoires de données et recharge les beans concernés.
 */
@Service
public class HotReloadService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotReloadService.class);
    
    @Autowired
    private ReloadableBeansManager reloadableBeansManager;
    
    private final Map<Path, Long> fileModificationTimes = new HashMap<>();
    private boolean enabled = true;
    
    /**
     * Vérifie toutes les 2 secondes si des fichiers ont été modifiés.
     * Utiliser @Scheduled uniquement si vous activez @EnableScheduling dans votre config.
     */
    @Scheduled(fixedDelay = 2000)
    public void checkForFileChanges() {
        if (!enabled) {
            return;
        }
        
        try {
            boolean hasChanges = false;
            
            // Vérifier les fichiers surveillés
            for (Path path : reloadableBeansManager.getWatchedPaths()) {
                if (Files.exists(path)) {
                    if (Files.isDirectory(path)) {
                        hasChanges |= checkDirectoryChanges(path);
                    } else {
                        hasChanges |= checkFileChange(path);
                    }
                }
            }
            
            if (hasChanges) {
                logger.info("🔄 Modifications détectées, rechargement des beans...");
                reloadableBeansManager.reloadBeans();
                logger.info("✅ Beans rechargés avec succès");
            }
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la vérification des fichiers", e);
        }
    }
    
    /**
     * Vérifie si un fichier a été modifié.
     */
    private boolean checkFileChange(Path file) throws IOException {
        long lastModified = Files.getLastModifiedTime(file).toMillis();
        Long previousModified = fileModificationTimes.get(file);
        
        if (previousModified == null || lastModified > previousModified) {
            fileModificationTimes.put(file, lastModified);
            if (previousModified != null) {
                logger.info("📝 Fichier modifié: {}", file.getFileName());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifie récursivement tous les fichiers d'un répertoire.
     */
    private boolean checkDirectoryChanges(Path directory) throws IOException {
        final boolean[] hasChanges = {false};
        
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".json")) {
                    hasChanges[0] |= checkFileChange(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        
        return hasChanges[0];
    }
    
    /**
     * Active/désactive le rechargement automatique.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        logger.info("Hot reload {}", enabled ? "activé" : "désactivé");
    }
    
    public boolean isEnabled() {
        return enabled;
    }
}