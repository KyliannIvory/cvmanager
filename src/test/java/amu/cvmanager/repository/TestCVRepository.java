package amu.cvmanager.repository;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCVRepository {


    static String activity_url;
    static RestTemplate restTemplate;
    static String url;
    static CV cv;

    @BeforeAll
    static void setUp(){
        restTemplate = new RestTemplate();
        url = "http://localhost:8080/cVs";
        activity_url = "http://localhost:8080/activities";
        cv = new CV();
    }


    @Test
    @Order(1)
    void testCreateCV(){
        ResponseEntity<CV> responseEntity =
                restTemplate.postForEntity(url, cv, CV.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @Order(2)
    void testGetCVById(){
        int id = 1;
        ResponseEntity<CV> response =
                restTemplate.getForEntity(url+"/"+id, CV.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(3)
    void testUpdateCV(){
        int id = 1;
        Activity activity1 = new Activity(2025, ActivityType.PROJECT,"cvmanager","Application de gestion de CV","www.cv.com");
        restTemplate.postForEntity(activity_url, activity1, Activity.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/uri-list"));

        HttpEntity<String> entity = new HttpEntity<>("http://localhost:8080/cVs/1", headers);
        ResponseEntity<String> response =
                restTemplate.exchange(activity_url+"/" + id +"/cv", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    @Order(4)
    void testDeleteCVById(){
        int id = 1;
        String completeUrl = url+"/"+id;

        ResponseEntity<Void> response =
                restTemplate.exchange(completeUrl, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }





}
