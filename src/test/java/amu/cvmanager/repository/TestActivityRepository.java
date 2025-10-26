package amu.cvmanager.repository;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityRepository {


    static RestTemplate restTemplate ;
    static String url;
    static Activity activity1;
    static Activity activity2;
    static Activity activity3;


    @BeforeAll
    static void setUp(){
        restTemplate = new RestTemplate();
        url = "http://localhost:8080/activities";

        activity1 = new Activity(2025, ActivityType.PROJECT,"cvmanager","Application de gestion de CV","www.cv.com");
        activity2 = new Activity(2026,ActivityType.PROFESSIONAL_EXPERIENCE,"Développeur Java", "Stagiaire en conceptoin d'application", null);
        activity3 = new Activity(2024, ActivityType.EDUCATION, "L3 info", "Licence 3 en informatique", "www.amu.com");
    }

    @Test
    @Order(1)
    void testCreateActivity(){

        ResponseEntity<Activity> response =
                restTemplate.postForEntity(url,activity1,Activity.class);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(activity1.getYear(),response.getBody().getYear());
        assertEquals(activity1.getType(),response.getBody().getType());
        assertEquals(activity1.getTitle(),response.getBody().getTitle());
        assertEquals(activity1.getDescription(),response.getBody().getDescription());
        assertEquals(activity1.getWebAddress() ,response.getBody().getWebAddress());

    }

    @Test
    @Order(2)
    void testGetActivityById(){
        int id = 1;
        ResponseEntity<Activity> response =
               restTemplate.getForEntity(url+"/"+id,Activity.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(activity1.getYear(),response.getBody().getYear());
        assertEquals(activity1.getType(),response.getBody().getType());
        assertEquals(activity1.getTitle(),response.getBody().getTitle());
        assertEquals(activity1.getDescription(),response.getBody().getDescription());
        assertEquals(activity1.getWebAddress() ,response.getBody().getWebAddress());

    }

    @Test
    @Order(3)
    void testGetAllActivities() throws JsonProcessingException {
        ResponseEntity<Activity> response =
                restTemplate.postForEntity(url,activity2,Activity.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        ResponseEntity<String> responseGetAllActivities =
                restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK,responseGetAllActivities.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseGetAllActivities.getBody());

        JsonNode activitiesNode = root.path("_embedded").path("activities");

        assertEquals(2, activitiesNode.size());
    }

    @Test
    @Order(4)
    void testUpdateActivity(){
        int id = 2;
        String completeUrl = url+"/"+id;
        activity2.setYear(2023);
        activity2.setTitle("Dev");

        HttpEntity<Activity> entity = new HttpEntity<>(activity2);
        ResponseEntity<Activity> response =
                restTemplate.exchange(completeUrl, HttpMethod.PUT, entity, Activity.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(activity2.getYear(),response.getBody().getYear());
        assertEquals(activity2.getTitle(),response.getBody().getTitle());
        assertEquals(activity2.getType(),response.getBody().getType());
        assertEquals(activity2.getDescription(),response.getBody().getDescription());
        assertEquals(activity2.getWebAddress(),response.getBody().getWebAddress());
    }

    @Test
    @Order(5)
    void testDeleteActivity(){
        int id = 1;
        String completeUrl = url+"/"+id;

        ResponseEntity<Void> response =
                restTemplate.exchange(completeUrl, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        ResponseEntity<Void> r =
                restTemplate.exchange(url+"/2", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertNull(response.getBody());

    }
}
