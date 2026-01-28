package com.towerdefense.http.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * Configuration CORS permissive pour le développement.
     * EN PRODUCTION : restreindre les origines autorisées !
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")  // ⚠️ DANGER en production ! Spécifier les origines exactes
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("*")
//            .allowCredentials(true)
            .maxAge(3600);
    }
}
