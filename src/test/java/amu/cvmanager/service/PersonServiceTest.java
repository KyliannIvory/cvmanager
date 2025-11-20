package amu.cvmanager.service;

import amu.cvmanager.exception.PersonNotFoundException;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import amu.cvmanager.model.Person;
import amu.cvmanager.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        // 1. Définition des personnes (avec constructeur Long ID, String lastName, ...)
        person1 = new Person(1L, "JAMES", "LeBron", "ljames@gmail.com", "ljames.com", "bronny",
                LocalDate.of(1984, Month.DECEMBER, 30), null);

        person2 = new Person(2L, "CURRY", "Stephen", "scurry@gmail.com", "scurry.com", "chef",
                LocalDate.of(1988, Month.MARCH, 14), null);

        // 2. Création d'une activité spécifique pour le test
        Activity activityCurry = new Activity(10L, 2020, ActivityType.PROJECT, "Basketball Star", "Winning the title", "nba.com/warriors", null);

        // 3. Création du CV mutable et attribution
        List<Activity> mutableActivities = new ArrayList<>();
        mutableActivities.add(activityCurry);
        CV cvCurry = new CV(mutableActivities);
        cvCurry.setId(20L);

        // 4. Lier l'activité/CV à la personne 2
        person2.assignCV(cvCurry);
        activityCurry.setCv(cvCurry); // Assurer la bidirectionnalité
    }

    @Test
    void testCreatePerson() {
        when(personRepository.save(any(Person.class))).thenReturn(person1);

        Person created = personService.createPerson(person1);

        assertNotNull(created);
        assertEquals(person1.getLastName(), created.getLastName());
        verify(personRepository, times(1)).save(person1);
    }

    @Test
    void testFindPersonById_Success() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person1));

        Person found = personService.findPersonById(1L);

        assertNotNull(found);
        assertEquals("JAMES", found.getLastName());
    }

    @Test
    void testFindPersonById_NotFound() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.findPersonById(99L));
    }

    @Test
    void testFindAllPersons() {
        List<Person> personList = Arrays.asList(person1, person2);
        when(personRepository.findAll()).thenReturn(personList);

        List<Person> foundList = personService.findAllpersons();

        assertNotNull(foundList);
        assertEquals(2, foundList.size());
        assertEquals("CURRY", foundList.get(1).getLastName());
    }

    @Test
    void testUpdatePerson() {
        // 1. Setup initial read
        when(personRepository.findById(1L)).thenReturn(Optional.of(person1));

        // Setup updated data
        Person updatedDetails = new Person();
        updatedDetails.setWebsite("newwebsite.com");
        updatedDetails.setFirstName("LeBron The King");

        // 2. Setup save operation to return the modified object (person1 state changes during update)
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Person result = personService.updatePerson(1L, updatedDetails);

        // Verify
        assertEquals("newwebsite.com", result.getWebsite());
        assertEquals("LeBron The King", result.getFirstName());
        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).save(person1);
    }

    @Test
    void testDeletePersonById() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person1));
        doNothing().when(personRepository).delete(person1);

        personService.deletePersonById(1L);

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).delete(person1);
    }

    @Test
    void testSearchPersons() {
        List<Person> personList = Collections.singletonList(person1);
        when(personRepository.searchPersons("james")).thenReturn(personList);

        List<Person> foundList = personService.searchPersons("james");

        assertNotNull(foundList);
        assertEquals(1, foundList.size());
        assertEquals("JAMES", foundList.getFirst().getLastName());
        verify(personRepository, times(1)).searchPersons("james");
    }

    @Test
    void testSearchPersons_ByLastNameOrFirstName() {
        // Recherche par Nom
        List<Person> resultLastName = Collections.singletonList(person1);
        when(personRepository.searchPersons("james")).thenReturn(resultLastName);

        List<Person> foundByLastName = personService.searchPersons("james");

        assertNotNull(foundByLastName);
        assertEquals(1, foundByLastName.size());
        assertEquals("JAMES", foundByLastName.get(0).getLastName());

        // Recherche par Prénom
        List<Person> resultFirstName = Collections.singletonList(person2);
        when(personRepository.searchPersons("stephen")).thenReturn(resultFirstName);

        List<Person> foundByFirstName = personService.searchPersons("stephen");

        assertNotNull(foundByFirstName);
        assertEquals(1, foundByFirstName.size());
        assertEquals("CURRY", foundByFirstName.get(0).getLastName());

        verify(personRepository, times(1)).searchPersons("james");
        verify(personRepository, times(1)).searchPersons("stephen");
    }

    @Test
    void testSearchPersons_ByActivityTitleAndCombinedSearch() {
        // Recherche par Titre d'Activité ("Basketball Star")
        List<Person> resultByActivity = Collections.singletonList(person2);
        when(personRepository.searchPersons("Star")).thenReturn(resultByActivity);

        List<Person> foundByActivity = personService.searchPersons("Star");

        assertNotNull(foundByActivity);
        assertEquals(1, foundByActivity.size());
        assertEquals("CURRY", foundByActivity.get(0).getLastName());

        // Recherche Combinée (un terme qui pourrait matcher James et Curry, ici simulé)
        List<Person> combinedResult = Arrays.asList(person1, person2);
        when(personRepository.searchPersons("e")).thenReturn(combinedResult);

        List<Person> foundCombined = personService.searchPersons("e"); // Matche L**e**Bron et St**e**ph**e**n

        assertNotNull(foundCombined);
        assertEquals(2, foundCombined.size());

        verify(personRepository, times(1)).searchPersons("Star");
        verify(personRepository, times(1)).searchPersons("e");
    }
}