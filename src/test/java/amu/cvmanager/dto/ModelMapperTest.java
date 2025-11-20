package amu.cvmanager.dto;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import amu.cvmanager.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperTest {

    private ModelMapper modelMapper;
    private Person person;


    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();

        // 1. Préparation des Entités
        Activity activity = new Activity(1L, 2024, ActivityType.PROJECT, "Projet JEE", "Description du projet d'archi", "http://projet.com", null);

        List<Activity> activities = new ArrayList<>();
        activities.add(activity);

        CV cv = new CV(activities);
        cv.setId(10L);
        activity.setCv(cv); // Établir la relation bidirectionnelle

        person = new Person(
                5L,
                "Kouame",
                "Kyliann",
                "kouame.kyliann@mail.com",
                "http://moncv.com",
                "motdepasse",
                LocalDate.of(1990, Month.JANUARY, 1),
                cv
        );
        cv.assignPerson(person);
    }

    @Test
    void testPersonEntityToPersonDTO_Mapping() {
        // Quand: Mapper l'Entité Person vers le DTO PersonDTO
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);

        // Alors: Vérification des champs et de la structure imbriquée
        assertNotNull(personDTO);
        assertEquals(5L, personDTO.getId());
        assertEquals("Kouame", personDTO.getLastName());

        // Vérifier le DTO CV imbriqué
        assertNotNull(personDTO.getCv());
        assertEquals(10L, personDTO.getCv().getId());

        // Vérifier le DTO Activité imbriqué
        assertFalse(personDTO.getCv().getActivities().isEmpty());
        ActivityDTO activityDTO = personDTO.getCv().getActivities().get(0);
        assertEquals("Projet JEE", activityDTO.getTitle());
        assertEquals(ActivityType.PROJECT, activityDTO.getType());
    }

    @Test
    void testPersonDTOToPersonEntity_Mapping() {
        // Étant donné: Créer un DTO pour la conversion inverse
        PersonDTO sourceDTO = new PersonDTO(
                6L,
                "Alice",
                "SMITH",
                "alice.smith@mail.com",
                "http://alicesmith.com",
                "newpassword",
                LocalDate.of(1995, Month.DECEMBER, 31),
                new CVDTO(20L, List.of(new ActivityDTO(
                        3L,
                        2022,
                        ActivityType.EDUCATION,
                        "Bac S",
                        "mention bien",
                        null // webAddress
                )))
        );

        // Quand: Mapper le DTO PersonDTO vers l'Entité Person
        Person targetPerson = modelMapper.map(sourceDTO, Person.class);

        // Alors: Vérification des champs
        assertNotNull(targetPerson);
        assertEquals("SMITH", targetPerson.getLastName());

        // Vérifier les données transférées à l'Entité CV et Activité
        assertNotNull(targetPerson.getCv());
        assertFalse(targetPerson.getCv().getActivities().isEmpty());

        Activity activityEntity = targetPerson.getCv().getActivities().getFirst();
        assertEquals("Bac S", activityEntity.getTitle());
    }
}