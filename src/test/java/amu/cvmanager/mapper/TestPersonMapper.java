package amu.cvmanager.mapper;

import amu.cvmanager.dto.ActivityDTO;
import amu.cvmanager.dto.CVDTO;
import amu.cvmanager.dto.PersonDTO;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import amu.cvmanager.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class TestPersonMapper {


    static Person person;
    static PersonDTO personDTO;
    static CV cv;
    static CVDTO cvDTO;
    static String firstName;
    static String lastName;
    static LocalDate birthDate;
    static String email;
    static String website;
    static String password;
    static PersonMapper personMapper;


    @BeforeAll
    static void setUp() {
        firstName = "LeBron";
        lastName = "James";
        birthDate = LocalDate.of(1984, Month.DECEMBER, 30);
        email = "ljames@mail.com";
        website = "www.ljames.com";
        password = "password";


        Activity activity1 = new Activity(1L, 2018, ActivityType.PROJECT, "CV Manager", "Description du projet", "www.cvmanager.org");
        Activity activity2 = new Activity(2L, 2019, ActivityType.PROFESSIONAL_EXPERIENCE, "Stage JEE", "Stage en entreprise", "www.stage.com");

        ActivityDTO activityDTO1 = new ActivityDTO(1L, 2018, ActivityType.PROJECT, "CV Manager", "Description du projet", "www.cvmanager.org");
        ActivityDTO activityDTO2 = new ActivityDTO(2L, 2019, ActivityType.PROFESSIONAL_EXPERIENCE, "Stage JEE", "Stage en entreprise", "www.stage.com");


        cv = new CV(1L, new ArrayList<>(List.of(activity1, activity2)));
        cvDTO = new CVDTO(1L, new ArrayList<>(List.of(activityDTO1, activityDTO2)));

        person = new Person(2L, lastName, firstName, email, website, password, birthDate, cv);
        personDTO = new PersonDTO(2L, firstName, lastName, email, website, password, birthDate, cvDTO);

        personMapper = new PersonMapper(new CVMapper(new ActivityMapper())) ;
    }


    @Test
    public void testFromDTO() {
        Person mappedPerson = personMapper.fromDTO(personDTO);

        assertNotNull(mappedPerson);
        assertEquals(personDTO.id(), mappedPerson.getId());
        assertEquals(personDTO.firstName(), mappedPerson.getFirstName());
        assertEquals(personDTO.lastName(), mappedPerson.getLastName());
        assertEquals(personDTO.email(), mappedPerson.getEmail());
        assertEquals(personDTO.website(), mappedPerson.getWebsite());
        assertEquals(personDTO.password(), mappedPerson.getPassword());
        assertEquals(personDTO.birthDate(), mappedPerson.getBirthDate());
        assertEquals(personDTO.cv().id(), mappedPerson.getCv().getId());
        assertEquals(personDTO.cv().activities().size(), mappedPerson.getCv().getActivities().size());

        CV mappedCV = mappedPerson.getCv();

        for (int i = 0; i< mappedCV.getActivities().size(); i++){
            Activity activity = mappedCV.getActivities().get(i);
            ActivityDTO activityDTO = cvDTO.activities().get(i);
            assertEquals(activityDTO.id(), activity.getId());
            assertEquals(activityDTO.year(), activity.getYear());
            assertEquals(activityDTO.type(), activity.getType());
            assertEquals(activityDTO.title(), activity.getTitle());
            assertEquals(activityDTO.description(), activity.getDescription());
            assertEquals(activityDTO.webAddress(), activity.getWebAddress());
        }
    }

    @Test
    public void testToDTO() {
        PersonDTO mappedPersonDTO = personMapper.toDTO(person);

        assertNotNull(mappedPersonDTO);
        assertEquals(person.getId(), mappedPersonDTO.id());
        assertEquals(person.getFirstName(), mappedPersonDTO.firstName());
        assertEquals(person.getLastName(), mappedPersonDTO.lastName());
        assertEquals(person.getEmail(), mappedPersonDTO.email());
        assertEquals(person.getWebsite(), mappedPersonDTO.website());
        assertEquals(person.getPassword(), mappedPersonDTO.password());
        assertEquals(person.getBirthDate(), mappedPersonDTO.birthDate());
        assertNotNull(mappedPersonDTO.cv());
        assertEquals(person.getCv().getId(), mappedPersonDTO.cv().id());
        assertEquals(person.getCv().getActivities().size(), mappedPersonDTO.cv().activities().size());

        List<Activity> activities = person.getCv().getActivities();
        List<ActivityDTO> activityDTOs = mappedPersonDTO.cv().activities();

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            ActivityDTO activityDTO = activityDTOs.get(i);
            assertEquals(activity.getId(), activityDTO.id());
            assertEquals(activity.getYear(), activityDTO.year());
            assertEquals(activity.getType(), activityDTO.type());
            assertEquals(activity.getTitle(), activityDTO.title());
            assertEquals(activity.getDescription(), activityDTO.description());
            assertEquals(activity.getWebAddress(), activityDTO.webAddress());
        }
    }

}
