package amu.cvmanager.service;

import amu.cvmanager.exception.ActivityNotFoundException;
import amu.cvmanager.exception.CVNotFoundException;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import amu.cvmanager.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private CVService cvService; // Nécessaire pour les tests de création/suppression

    @InjectMocks
    private ActivityService activityService;

    private Activity activity1;
    private Activity activity2;
    private CV cv;

    @BeforeEach
    void setUp() {
        cv = new CV(new ArrayList<>());
        cv.setId(10L);

        activity1 = new Activity(1L, 2025, ActivityType.PROJECT, "cvmanager", "desc1", "web1", cv);
        activity2 = new Activity(2L, 2024, ActivityType.EDUCATION, "Master", "desc2", "web2", null);

        // Assurez-vous que le CV connaît l'activité pour l'état initial
        cv.getActivities().add(activity1);
    }

    @Test
    void testCreateActivity_Success() {
        // Étant donné : nouvelle activité qui sera ajoutée au CV existant
        Activity newActivity = new Activity(2023, ActivityType.OTHER, "Hobby", "desc3", "web3", null);

        when(cvService.findCVById(10L)).thenReturn(cv);
        when(activityRepository.save(any(Activity.class))).thenReturn(newActivity);

        // Quand
        Activity createdActivity = activityService.createActivity(10L, newActivity);

        // Alors
        assertNotNull(createdActivity);
        assertEquals(cv, createdActivity.getCv()); // Vérifie que l'activité a été liée au CV
        assertEquals(2, cv.getActivities().size()); // Vérifie que le CV a bien la nouvelle activité
        verify(activityRepository, times(1)).save(newActivity);
    }

    @Test
    void testCreateActivity_CVNotFound() {
        // Étant donné
        Activity newActivity = new Activity(2023, ActivityType.OTHER, "Hobby", "desc3", "web3", null);
        when(cvService.findCVById(99L)).thenThrow(new CVNotFoundException("CV not found"));

        // Alors
        assertThrows(CVNotFoundException.class, () -> activityService.createActivity(99L, newActivity));
        verify(activityRepository, never()).save(any());
    }


    @Test
    void testFindActivityById_Success() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity1));

        Activity found = activityService.findActivityById(1L);

        assertNotNull(found);
        assertEquals("cvmanager", found.getTitle());
    }

    @Test
    void testFindActivityByName_Success() {
        when(activityRepository.findByTitleLike("cvmanager")).thenReturn(Optional.of(activity1));

        Activity found = activityService.findActivityByName("cvmanager");

        assertNotNull(found);
        assertEquals("cvmanager", found.getTitle());
    }

    @Test
    void testFindActivityById_NotFound() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class, () -> activityService.findActivityById(99L));
    }

    @Test
    void testFindAllActivities() {
        List<Activity> activityList = Arrays.asList(activity1, activity2);
        when(activityRepository.findAll()).thenReturn(activityList);

        List<Activity> foundList = activityService.findAllActivities();

        assertNotNull(foundList);
        assertEquals(2, foundList.size());
    }

    @Test
    void testUpdateActivity() {
        // 1. Setup initial read
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity1));

        // Setup updated data
        Activity updatedDetails = new Activity();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setYear(2026);

        // 2. Setup save operation to return the modified object
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Activity result = activityService.updateActivity(1L, updatedDetails);

        // Verify
        assertEquals("Updated Title", result.getTitle());
        assertEquals(2026, result.getYear());
        verify(activityRepository, times(1)).findById(1L);
        verify(activityRepository, times(1)).save(activity1);
    }

    @Test
    void testDeleteActivityById() {
        // Étant donné : l'activité appartient à un CV
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity1));
        doNothing().when(activityRepository).deleteById(1L);

        // Quand
        activityService.deleteActivityById(1L);

        // Alors : vérifie que la relation avec le CV a été brisée avant la suppression
        assertFalse(cv.getActivities().contains(activity1));
        verify(activityRepository, times(1)).findById(1L);
        verify(activityRepository, times(1)).deleteById(1L);
    }
}