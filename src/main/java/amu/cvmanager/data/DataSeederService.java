package amu.cvmanager.data;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import amu.cvmanager.model.Person;
import amu.cvmanager.repository.ActivityRepository;
import amu.cvmanager.repository.CVRepository;
import amu.cvmanager.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Random;

@Service
public class DataSeederService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    CVRepository cvRepository;

    private final Random random = new Random();

    private static final String[] FIRST_NAMES = {"Alice", "Bob", "Clara", "David", "Emma", "Felix", "Gina", "Hugo", "Iris", "Jacques"};
    private static final String[] LAST_NAMES = {"Dupont", "Martin", "Lefebvre", "Dubois", "Thomas", "Leroy", "Moreau", "Simon", "Garcia", "Roux"};
    private static final ActivityType[] TYPES = ActivityType.values();




    // La transaction est ici, sur la méthode publique, garantissant son application.
    @Transactional
    public void seedDatabase() {
        // 1. Création de l'utilisateur de test (si non existant)
        if (personRepository.findByEmail("test@amu.fr").isEmpty()) {
            createTestUser("test@amu.fr", "motdepasse", "Admin");
            System.out.println("✅ UTILISATEUR DE TEST CRÉÉ : Email: test@amu.fr, Mot de passe: motdepasse");
        }

        // 2. Peuplement des CVs
        if (personRepository.findAll().size() < 10) {
            createDemoUsers(100);
            System.out.println("✅ PEUPLEMENT RÉUSSI : 100 utilisateurs/CVs de démo créés.");
        }
    }

    // Logique pour créer l'utilisateur unique (CV vide)
    private void createTestUser(String email, String rawPassword, String firstName) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        Person testUser = new Person(
                "ADMIN",
                firstName,
                email,
                "admin.com",
                hashedPassword,
                LocalDate.of(1995, Month.JUNE, 1),
                new CV(new ArrayList<>())
        );
        personRepository.save(testUser);
    }

    // Logique pour créer N personnes avec CVs et Activités
    private void createDemoUsers(int count) {
        for (int i = 1; i <= count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String email = "personne" + i + "@demo.com";

            // Création de 2 à 4 activités aléatoires
            CV cv = new CV(new ArrayList<>());

            int numActivities = 2 + random.nextInt(3);
            for (int j = 0; j < numActivities; j++) {
                ActivityType type = TYPES[random.nextInt(TYPES.length)];
                int year = 2024 - random.nextInt(10);
                String title = firstName + " - " + type.name().replace("_", " ") + " " + year;

                Activity activity = new Activity();
                activity.setYear(year);
                activity.setType(type);
                activity.setTitle(title);
                activity.setDescription("Description de l'activité numéro " + j);
                activity.setWebAddress("http://demo-web.com");

                // L'appel à addActivity (méthode de CV) est critique
                cv.addActivity(activity);
            }

            // Création et sauvegarde de la personne (le reste cascade)
            String rawPassword = "password" + i;
            String hashedPassword = passwordEncoder.encode(rawPassword);

            Person person = new Person(
                    lastName,
                    firstName,
                    email,
                    "http://cv-demo.com",
                    hashedPassword,
                    LocalDate.of(1980 + random.nextInt(20), Month.JANUARY, 1),
                    cv
            );

            cv.assignPerson(person);
            personRepository.save(person);
        }
    }
    public String getStats(){
        return "---------------------------------------------------------------------------------\n" +
                "---------------------------------------------------------------------------------\n" +
                "Il existe actuellement : \n " +
                personRepository.findAll().size() + " personnes, " +
                cvRepository.findAll().size() + " cv " +
                activityRepository.findAll().size() + " activités dans la BDD\n" +
                "---------------------------------------------------------------------------------\n" +
                "---------------------------------------------------------------------------------\n";
    }
}