package amu.cvmanager.repository;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.Optional;

@DataJpaTest
public class CVRepositoryTest {

    @Autowired
    CVRepository cvRepository;

    CV cv;

    @BeforeEach
    void setup(){
        Activity activity1 = new Activity(2025,  ActivityType.PROJECT, "cvmanager", "description de cvmanager", "www.cvmanager.ci");
        Activity activity2 = new Activity(2024,  ActivityType.PROFESSIONAL_EXPERIENCE, "stage", "description de stage","www.internship.com");

        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(activity1);
        activities.add(activity2);

        cv = new CV(activities);
    }

    @Test
    void testCreateCV(){
        CV savedCv = cvRepository.save(cv);

        assertNotEquals(0,savedCv.getId());
        assertEquals(2, savedCv.getActivities().size());
    }

    @Test
    void testReadCV(){
        CV savedCv = cvRepository.save(cv);
        Optional<CV> foundCv = cvRepository.findById(savedCv.getId());

        assertTrue(foundCv.isPresent());
        assertEquals(2, foundCv.get().getActivities().size());
    }

    @Test
    void testUpdateCV(){
        CV savedCv = cvRepository.save(cv);
        Activity activity = new Activity(2023,  ActivityType.EDUCATION, "L3 info", "description de la licence", "www.amu.fr");
        savedCv.getActivities().add(activity);

        cvRepository.save(savedCv);
        Optional<CV> foundCv = cvRepository.findById(savedCv.getId());
        assertTrue(foundCv.isPresent());

        assertEquals(3, foundCv.get().getActivities().size());
    }

    @Test
    void testDeleteCV(){
        CV savedCv = cvRepository.save(cv);
        Optional<CV> foundCv = cvRepository.findById(savedCv.getId());
        assertTrue(foundCv.isPresent());

        cvRepository.delete(savedCv);
        Optional<CV> foundCv1 = cvRepository.findById(savedCv.getId());
        assertFalse(foundCv1.isPresent());
    }
}
