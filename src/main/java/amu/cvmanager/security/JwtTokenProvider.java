package amu.cvmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Clé secrète stockée en base64 dans application.properties
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    // Durée de validité du jeton (par exemple, 7 jours)
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Générer un JWT
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(email) // Le sujet est l'email de l'utilisateur
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    // Obtenir la clé secrète
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Obtenir l'email depuis le JWT
    public String getEmailFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Valider le JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            // Loguer l'exception ici (e.g., SignatureException, ExpiredJwtException, etc.)
            // Pour l'instant, on retourne false
            return false;
        }
    }
}