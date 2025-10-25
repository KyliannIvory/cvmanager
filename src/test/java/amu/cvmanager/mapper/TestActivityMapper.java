package amu.cvmanager.mapper;

import amu.cvmanager.dto.ActivityDTO;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestActivityMapper {

    static Activity activity;
    static ActivityDTO activityDTO;
    static String title;
    static String description;
    static String webAddress;
    static ActivityMapper mapper;

    @BeforeAll
    public static void setUp() {
        title  = "cvmanager";
        description = "L'objectif du mini projet est simple : gérer à l'aide de la technologie JEE une liste de CVs.";
        webAddress = "www.cvmanager.org";

        activity = new Activity(1L,2018, ActivityType.PROJECT, title, description, webAddress);
        activityDTO = new ActivityDTO(1L,2018, ActivityType.PROJECT, title, description, webAddress);

        mapper = new ActivityMapper();
    }


    @Test
    void testFromDTO(){
        Activity mappedActivity = mapper.fromDTO(activityDTO);

        assertNotNull(mappedActivity);
        assertEquals(activityDTO.id(),mappedActivity.getId());
        assertEquals(activityDTO.year(),mappedActivity.getYear());
        assertEquals(activityDTO.type(),mappedActivity.getType());
        assertEquals(activityDTO.title(),mappedActivity.getTitle());
        assertEquals(activityDTO.description(),mappedActivity.getDescription());
        assertEquals(activityDTO.webAddress(),mappedActivity.getWebAddress());
    }

    @Test
    void testToDTO(){
        ActivityDTO mappedActivityDTO = mapper.toDTO(activity);

        assertNotNull(mappedActivityDTO);
        assertEquals(activity.getId(),mappedActivityDTO.id());
        assertEquals(activity.getYear(),mappedActivityDTO.year());
        assertEquals(activity.getType(),mappedActivityDTO.type());
        assertEquals(activity.getTitle(),mappedActivityDTO.title());
        assertEquals(activity.getDescription(),mappedActivityDTO.description());
        assertEquals(activity.getWebAddress(),mappedActivityDTO.webAddress());
    }
}
