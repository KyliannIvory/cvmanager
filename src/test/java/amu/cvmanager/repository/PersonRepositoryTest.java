package amu.cvmanager.repository;

import amu.cvmanager.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    Person person;

    @BeforeEach
    public void setup() {
        person = new Person(
                "JAMES",
                "LeBron",
                "ljames@gmail.com",
                "ljames.com",
                "bronny",
                LocalDate.of(1984, Month.DECEMBER,30)
        );
    }

    @Test
    void testCreatePerson() {
        Person savedPerson = personRepository.save(person);

        assertNotEquals(0, savedPerson.getId());
        assertEquals(person.getLastName(), savedPerson.getLastName());
    }

    @Test
    void testReadPerson() {
        Person savedPerson = personRepository.save(person);
        Optional<Person> foundedPerson = personRepository.findById(savedPerson.getId());

        assertTrue(foundedPerson.isPresent());
        assertEquals(foundedPerson.get().getLastName(), savedPerson.getLastName());
    }

    @Test
    void testUpdatePerson() {
        Person savedPerson = personRepository.save(person);

        String newWebsite = "lebronTheKingJames.com";
        savedPerson.setWebsite(newWebsite);
        personRepository.save(savedPerson);

        Optional<Person> foundedPerson = personRepository.findById(savedPerson.getId());
        assertTrue(foundedPerson.isPresent());

        assertEquals(newWebsite, foundedPerson.get().getWebsite());
    }


    @Test
    void testDeletePerson() {
        Person savedPerson = personRepository.save(person);
        Optional<Person> foundedPerson = personRepository.findById(savedPerson.getId());
        assertTrue(foundedPerson.isPresent());

        personRepository.delete(foundedPerson.get());
        Optional<Person> deletedPerson = personRepository.findById(savedPerson.getId());
        assertFalse(deletedPerson.isPresent());
    }
}
