package amu.cvmanager.mapper;

import amu.cvmanager.dto.ActivityDTO;
import amu.cvmanager.dto.CVDTO;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCVMapper {

    static CV cv;
    static CVDTO cvDTO;
    static List<Activity> activities;
    static List<ActivityDTO> activitiesDTO;
    static CVMapper cvMapper;

    @BeforeAll
    public static void setUp() {
        cvMapper= new CVMapper(new ActivityMapper()) ;

        Activity activity1 = new Activity(1L, 2018, ActivityType.PROJECT, "CV Manager", "Description du projet", "www.cvmanager.org");
        Activity activity2 = new Activity(2L, 2019, ActivityType.PROFESSIONAL_EXPERIENCE, "Stage JEE", "Stage en entreprise", "www.stage.com");

        activities = new ArrayList<>();
        activities.add(activity1);
        activities.add(activity2);

        ActivityDTO activityDTO1 = new ActivityDTO(1L, 2018, ActivityType.PROJECT, "CV Manager", "Description du projet", "www.cvmanager.org");
        ActivityDTO activityDTO2 = new ActivityDTO(2L, 2019, ActivityType.PROFESSIONAL_EXPERIENCE, "Stage JEE", "Stage en entreprise", "www.stage.com");

        activitiesDTO = new ArrayList<>();
        activitiesDTO.add(activityDTO1);
        activitiesDTO.add(activityDTO2);

        cv = new CV(1L, activities);
        cvDTO = new CVDTO(1L, activitiesDTO);
    }

    @Test
    void testFromDTO() {
        CV mappedCV = cvMapper.fromDTO(cvDTO);

        assertNotNull(mappedCV);
        assertEquals(cvDTO.id(), mappedCV.getId());
        assertNotNull(mappedCV.getActivities());
        assertEquals(cvDTO.activities().size(), mappedCV.getActivities().size());

        for (int i = 0; i < mappedCV.getActivities().size(); i++) {
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
    void testToDTO() {
        CVDTO mappedCVDTO = cvMapper.toDTO(cv);

        assertNotNull(mappedCVDTO);
        assertEquals(cv.getId(), mappedCVDTO.id());
        assertNotNull(mappedCVDTO.activities());
        assertEquals(cv.getActivities().size(), mappedCVDTO.activities().size());

        for (int i = 0; i < mappedCVDTO.activities().size(); i++) {
            ActivityDTO dto = mappedCVDTO.activities().get(i);
            Activity activity = cv.getActivities().get(i);
            assertEquals(activity.getId(), dto.id());
            assertEquals(activity.getYear(), dto.year());
            assertEquals(activity.getType(), dto.type());
            assertEquals(activity.getTitle(), dto.title());
            assertEquals(activity.getDescription(), dto.description());
            assertEquals(activity.getWebAddress(), dto.webAddress());
        }
    }
}
