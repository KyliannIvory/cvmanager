package amu.cvmanager.repository;

import amu.cvmanager.model.Person;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPersonRepository {

    static RestTemplate restTemplate;
    static String url;
    static Person person;

    @BeforeAll
    static void setUp() {
        restTemplate = new RestTemplate();
        url = "http://localhost:8080/persons";
        person = new Person("James", "Lebron", "ljames@mail.com", null, "bronny", LocalDate.of(1984, Month.DECEMBER, 30));
    }

    @Test
    @Order(1)
    void testCreatePerson() {

        ResponseEntity<Person> response =
                restTemplate.postForEntity(url, person, Person.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(person.getLastName(), response.getBody().getLastName());
        assertEquals(person.getFirstName(), response.getBody().getFirstName());
        assertEquals(person.getEmail(), response.getBody().getEmail());
        assertEquals(person.getPassword(), response.getBody().getPassword());
        assertEquals(person.getBirthDate(), response.getBody().getBirthDate());
    }


    @Test
    @Order(2)
    void tetGetPersonById() {
        int id = 1;
        ResponseEntity<Person> response =
                restTemplate.getForEntity(url + "/" + id, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(person.getLastName(), response.getBody().getLastName());
        assertEquals(person.getFirstName(), response.getBody().getFirstName());
        assertEquals(person.getEmail(), response.getBody().getEmail());
        assertEquals(person.getPassword(), response.getBody().getPassword());
        assertEquals(person.getBirthDate(), response.getBody().getBirthDate());
    }


    @Test
    @Order(3)
    void testUpdatePerson() {
        int id = 1;
        String completeUrl = url + "/" + id;
        person.setLastName("Durant");
        person.setFirstName("Kevin");

        HttpEntity<Person> request = new HttpEntity<>(person);
        ResponseEntity<Person> response =
                restTemplate.exchange(completeUrl, HttpMethod.PUT, request, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(person.getLastName(), response.getBody().getLastName());
        assertEquals(person.getFirstName(), response.getBody().getFirstName());
        assertEquals(person.getEmail(), response.getBody().getEmail());
        assertEquals(person.getPassword(), response.getBody().getPassword());
        assertEquals(person.getBirthDate(), response.getBody().getBirthDate());
    }

    @Test
    @Order(4)
    void testDeletePerson() {
        int id = 1;
        String completeUrl = url + "/" + id;

        ResponseEntity<Void> response =
                restTemplate.exchange(completeUrl, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }


}
