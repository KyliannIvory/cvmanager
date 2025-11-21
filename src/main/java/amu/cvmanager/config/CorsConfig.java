package amu.cvmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Appliquer à tous les chemins de l'API
                .allowedOrigins("http://localhost:5173") // ⬅️ Origine de votre serveur de développement VueJS
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Autoriser les méthodes utilisées
                .allowedHeaders("*") // Autoriser tous les headers
                .allowCredentials(true); // Autoriser l'envoi de cookies/jetons (si besoin, mais bonne pratique ici)
    }
}