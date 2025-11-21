package amu.cvmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraire le JWT du Header de la requête (Bearer <token>)
        String token = getJwtFromRequest(request);

        // 2. Valider le token et charger l'utilisateur
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

            // Récupérer l'email (subject) du token
            String email = tokenProvider.getEmailFromJwt(token);

            // Charger les détails de l'utilisateur par l'email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Créer l'objet d'authentification
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities() // Collections.emptyList() dans notre cas
            );

            // Ajouter les détails de la requête
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Définir l'utilisateur comme authentifié dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    // Méthode utilitaire pour extraire le JWT du header Authorization
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Vérifie si le header commence par "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Renvoie tout après "Bearer "
        }
        return null;
    }
}