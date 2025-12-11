package amu.cvmanager.controller;

import amu.cvmanager.dto.LoginDTO;
import amu.cvmanager.dto.LoginResponseDTO;
import amu.cvmanager.model.Person;
import amu.cvmanager.security.JwtTokenProvider;
import amu.cvmanager.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonService personService; // ⬅️ PersonService injecté

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService; // ⬅️ PersonService injecté
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody LoginDTO loginDto) {

        // 1. Authentification de l'utilisateur (vérification du mot de passe haché)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        // 2. Si l'authentification réussit, met à jour le contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Génère le jeton JWT
        String token = jwtTokenProvider.generateToken(authentication);

        // 4. Récupération des informations de la Personne pour la réponse frontend
        String userEmail = authentication.getName();
        Person person = personService.findPersonByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé après l'authentification."));

        // 5. Construction du DTO de réponse complet
        LoginResponseDTO response = new LoginResponseDTO(
                token,
                person.getId(),
                person.getEmail(),
                person.getFirstName(),
                person.getLastName()
        );

        // 6. Renvoie le DTO avec les infos et le jeton
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}