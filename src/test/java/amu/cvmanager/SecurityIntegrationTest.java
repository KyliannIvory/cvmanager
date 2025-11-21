package amu.cvmanager;

import amu.cvmanager.dto.LoginDTO;
import amu.cvmanager.dto.PersonDTO;
import amu.cvmanager.model.Person;
import amu.cvmanager.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional // Assure que les données H2 sont nettoyées après chaque test
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String LOGIN_URL = "/api/auth/login";
    private static final String PERSONS_URL = "/cvmanager/persons";
    private static final String PUBLIC_EMAIL = "public.user@amu.fr";
    private static final String PUBLIC_PASSWORD = "password123";

    @BeforeEach
    void setup() {
        // Crée un utilisateur de base dans la BD H2 avec un mot de passe haché
        Person existingPerson = new Person();
        existingPerson.setLastName("TEST");
        existingPerson.setFirstName("Security");
        existingPerson.setEmail(PUBLIC_EMAIL);
        existingPerson.setPassword(passwordEncoder.encode(PUBLIC_PASSWORD)); // Hachage
        existingPerson.setBirthDate(LocalDate.of(2000, Month.JANUARY, 1));
        personRepository.save(existingPerson);
    }

    // =======================================================================
    // 1. Tests d'Authentification (Login)
    // =======================================================================

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO loginDto = new LoginDTO(PUBLIC_EMAIL, PUBLIC_PASSWORD);

        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Vérifie que le token JWT est retourné (doit être une chaîne non vide)
        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.length() > 50, "Le token JWT devrait être retourné.");
    }

    @Test
    void testLogin_Failure_WrongPassword() throws Exception {
        LoginDTO loginDto = new LoginDTO(PUBLIC_EMAIL, "wrongpassword");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized()); // Doit échouer avec 401
    }

    // =======================================================================
    // 2. Tests d'Accès Non Authentifié (Public vs Protégé)
    // =======================================================================

    @Test
    void testAccess_PublicEndpoint_GET_Success() throws Exception {
        // GET /cvmanager/persons est public selon SecurityConfig
        mockMvc.perform(get(PERSONS_URL))
                .andExpect(status().isOk());
    }

    @Test
    void testAccess_ProtectedEndpoint_POST_Failure() throws Exception {
        PersonDTO newPerson = PersonDTO.builder().firstName("Fail").lastName("Test").email("fail@mail.com").password("pwd").build();

        // POST /cvmanager/persons doit être protégé (cooptation)
        mockMvc.perform(post(PERSONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isUnauthorized()); // Doit échouer avec 401
    }

    @Test
    void testAccess_ProtectedEndpoint_DELETE_Failure() throws Exception {
        // DELETE /cvmanager/persons/{id} doit être protégé
        mockMvc.perform(delete(PERSONS_URL + "/1"))
                .andExpect(status().isUnauthorized()); // Doit échouer avec 401
    }

    // =======================================================================
    // 3. Tests d'Accès Authentifié (Token Valide)
    // =======================================================================

    private String authenticateAndGetToken() throws Exception {
        LoginDTO loginDto = new LoginDTO(PUBLIC_EMAIL, PUBLIC_PASSWORD);
        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    @Test
    void testAccess_ProtectedEndpoint_POST_Success() throws Exception {
        String jwtToken = authenticateAndGetToken();

        // Nouvelle personne à créer par cooptation
        PersonDTO cooptedPerson = PersonDTO.builder()
                .firstName("New")
                .lastName("Coopted")
                .email("coopted@mail.com")
                .password("securepwd")
                .birthDate(LocalDate.of(2001, 1, 1))
                .build();

        // La requête POST réussit car le token est fourni
        mockMvc.perform(post(PERSONS_URL)
                        .header("Authorization", "Bearer " + jwtToken) // Ajout du JWT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cooptedPerson)))
                .andExpect(status().isOk()); // Création réussie (200 OK du controller)
    }

    @Test
    void testAccess_ProtectedEndpoint_PUT_Success() throws Exception {
        String jwtToken = authenticateAndGetToken();

        // Utilisateur existant dans la BD (ID 1 par convention H2, ou l'ID généré)
        Person existing = personRepository.findByEmail(PUBLIC_EMAIL).get();

        PersonDTO updatedPerson = PersonDTO.builder()
                .firstName("Updated")
                .lastName(existing.getLastName())
                .email(existing.getEmail())
                .password(PUBLIC_PASSWORD) // Non utilisé par PUT, mais doit être présent
                .build();

        // La requête PUT réussit
        mockMvc.perform(put(PERSONS_URL + "/" + existing.getId())
                        .header("Authorization", "Bearer " + jwtToken) // Ajout du JWT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk());
    }

    @Test
    void testAccess_ProtectedEndpoint_DELETE_Success() throws Exception {
        String jwtToken = authenticateAndGetToken();

        // Utilisateur existant (ID 1 par convention H2, ou l'ID généré)
        Person existing = personRepository.findByEmail(PUBLIC_EMAIL).get();

        // La requête DELETE réussit
        mockMvc.perform(delete(PERSONS_URL + "/" + existing.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }
}