package amu.cvmanager.repository;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    private Activity activity;

    @BeforeEach
     void setup() {
        activity = new Activity(2025,  ActivityType.PROJECT, "cvmanager", "description de cvmanager", "www.cvmanager.ci", new CV());
    }

    @Test
    void testCreateActivity() {
        Activity savedActivity = activityRepository.save(activity);

        assertNotEquals(0,savedActivity.getId());
        assertEquals(activity.getTitle(),savedActivity.getTitle());
    }

    @Test
    void testReadActivity() {
        Activity savedActivity =  activityRepository.save(activity);
        Optional<Activity> foundedActivity = activityRepository.findById(savedActivity.getId());

        assertTrue(foundedActivity.isPresent());
        assertEquals(activity.getTitle(),foundedActivity.get().getTitle());
    }

    @Test
    void testUpdateActivity() {
        String updatedTitle = "updated title";
        Activity savedActivity = activityRepository.save(activity);
        savedActivity.setTitle(updatedTitle);

        Activity updedActivity = activityRepository.save(savedActivity);
        Optional <Activity> foundedActivity = activityRepository.findById(updedActivity.getId());

        assertTrue(foundedActivity.isPresent());
        assertEquals(updatedTitle,foundedActivity.get().getTitle());
    }

    @Test
    void testDeleteActivity() {
        Activity savedActivity = activityRepository.save(activity);
        Optional<Activity> foundedActivity = activityRepository.findById(savedActivity.getId());
        assertTrue(foundedActivity.isPresent());

        activityRepository.delete(savedActivity);
        Optional<Activity> foundedActivity2 = activityRepository.findById(savedActivity.getId());
        assertFalse(foundedActivity2.isPresent());
    }
}
