package amu.cvmanager.service;

import amu.cvmanager.exception.CVNotFoundException;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.ActivityType;
import amu.cvmanager.model.CV;
import amu.cvmanager.repository.CVRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CVServiceTest {

    @Mock
    private CVRepository cvRepository;

    @InjectMocks
    private CVService cvService;

    private CV cv1;
    private CV cv2;
    private Activity activity;

    @BeforeEach
    void setUp() {
        activity = new Activity(2025, ActivityType.PROJECT, "cvmanager", "desc", "web", null);

        List<Activity> mutableActivities = new ArrayList<>();
        mutableActivities.add(activity);

        cv1 = new CV(mutableActivities);
        cv1.setId(1L);


        cv2 = new CV(Collections.emptyList());
        cv2.setId(2L);
    }

    @Test
    void testCreateCV() {
        // Étant donné
        when(cvRepository.save(any(CV.class))).thenReturn(cv1);

        // Quand
        CV createdCV = cvService.createCV(cv1);

        // Alors
        assertNotNull(createdCV);
        assertEquals(1, createdCV.getActivities().size());
        verify(cvRepository, times(1)).save(cv1);
    }

    @Test
    void testFindCVById_Success() {
        // Étant donné
        when(cvRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(cv1));

        // Quand
        CV foundCV = cvService.findCVById(1L);

        // Alors
        assertNotNull(foundCV);
        assertEquals(1L, foundCV.getId());
    }

    @Test
    void testFindCVById_NotFound() {
        // Étant donné
        when(cvRepository.findById(Long.valueOf(99L))).thenReturn(Optional.empty());

        // Alors
        assertThrows(CVNotFoundException.class, () -> cvService.findCVById(99L));
    }

    @Test
    void testFindAllCVs() {
        // Étant donné
        List<CV> cvList = Arrays.asList(cv1, cv2);
        when(cvRepository.findAll()).thenReturn(cvList);

        // Quand
        List<CV> foundList = cvService.findAllCVs();

        // Alors
        assertNotNull(foundList);
        assertEquals(2, foundList.size());
    }

    @Test
    void testUpdateCV() {
        // Étant donné
        when(cvRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(cv1));

        Activity newActivity = new Activity(2023, ActivityType.EDUCATION, "Master", "desc master", "amu.fr", null);
        CV updatedDetails = new CV(Collections.singletonList(newActivity));

        when(cvRepository.save(any(CV.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Quand
        CV result = cvService.updateCV(1L, updatedDetails);

        // Alors
        assertEquals(1L, result.getId());
        assertEquals(1, result.getActivities().size());
        assertEquals("Master", result.getActivities().get(0).getTitle());
        verify(cvRepository, times(1)).findById(Long.valueOf(1L));
        verify(cvRepository, times(1)).save(cv1);
    }

    @Test
    void testDeleteCVById() {
        // Étant donné
        when(cvRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(cv1));
        doNothing().when(cvRepository).deleteById(Long.valueOf(1L));

        // Quand
        cvService.deleteCVById(1L);

        // Alors
        verify(cvRepository, times(1)).findById(Long.valueOf(1L));
        verify(cvRepository, times(1)).deleteById(Long.valueOf(1L));
    }
}