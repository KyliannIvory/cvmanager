package amu.cvmanager.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Cette méthode est appelée lorsque l'utilisateur tente d'accéder à une ressource sécurisée
    // sans aucune ou avec une authentification invalide.
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Envoie une réponse d'erreur 401 (Unauthorized)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}