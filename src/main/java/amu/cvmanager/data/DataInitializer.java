package amu.cvmanager.data;

import amu.cvmanager.model.Person;
import amu.cvmanager.repository.PersonRepository;
import amu.cvmanager.service.PersonService;
import jakarta.annotation.PostConstruct; // Import pour l'annotation
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component; // Changement de @Configuration à @Component

import java.time.LocalDate;
import java.time.Month;

@Component // Utilisez @Component ou @Configuration (si vous définissiez d'autres beans)
public class DataInitializer {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonService personService;

    // Injection via constructeur (Meilleure pratique)
    public DataInitializer(PersonRepository personRepository, PasswordEncoder passwordEncoder, PersonService personService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.personService = personService;
    }

    @PostConstruct // Indique à Spring d'exécuter cette méthode APRÈS l'injection des dépendances
    public void initTestUser() {
        // La logique reste la même : vérifier et créer si l'utilisateur n'existe pas
        if (personRepository.findByEmail("test@amu.fr").isEmpty()) {

            String hashedPassword = passwordEncoder.encode("motdepasse");

            Person testUser = new Person("TEST", "Utilisateur", "test@amu.fr", "test.com", hashedPassword, LocalDate.of(1995, Month.JUNE, 1));

            personRepository.save(testUser);

            System.out.println("✅ UTILISATEUR DE TEST CRÉÉ :"+personService.findPersonByEmail(") Email: test@amu.fr, Mot de passe: motdepasse"));
        }
    }
}