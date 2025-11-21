package amu.cvmanager.security;

import amu.cvmanager.model.Person;
import amu.cvmanager.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Assure que la BD est remise à zéro après le test
class LoginIntegrationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonService personService;

    private static final String EMAIL_VALIDE = "login.test@amu.fr";
    private static final String PASSWORD_VALIDE = "passwordTest123";

    @BeforeEach
    void setup() {
        // 1. Création de la personne via le service
        // Le service s'occupe du hachage du mot de passe
        if (personService.findPersonByEmail(EMAIL_VALIDE).isEmpty()) {
            Person newPerson = new Person(
                    "Login",
                    "Tester",
                    EMAIL_VALIDE,
                    "test.com",
                    PASSWORD_VALIDE, // Le service va hacher ceci
                    LocalDate.of(2000, 1, 1)
            );
            personService.createPerson(newPerson);
        }
    }

    @Test
    void testLogin_Success_HashedPasswordVerification() {
        // Étant donné : Les identifiants corrects
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(EMAIL_VALIDE, PASSWORD_VALIDE);

        // Quand : On tente de s'authentifier
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Alors : L'authentification doit réussir (être authentifiée)
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated(), "L'utilisateur doit être authentifié après le login.");
        assertEquals(EMAIL_VALIDE, authentication.getName(), "L'identifiant de l'utilisateur doit correspondre à l'email.");
    }

    @Test
    void testLogin_Failure_WrongPassword() {
        // Étant donné : Un mot de passe incorrect
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(EMAIL_VALIDE, "mauvais_mot_de_passe");

        // Alors : Une exception BadCredentialsException doit être levée
        assertThrows(BadCredentialsException.class, () -> {
            authenticationManager.authenticate(authToken);
        }, "Le login doit échouer avec un mot de passe incorrect.");
    }
}