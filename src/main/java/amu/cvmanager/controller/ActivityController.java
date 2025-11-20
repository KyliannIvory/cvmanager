package amu.cvmanager.controller;

import amu.cvmanager.dto.ActivityDTO;
import amu.cvmanager.model.Activity;
import amu.cvmanager.service.ActivityService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cvmanager/activities")
public class ActivityController {

    private final ActivityService service;
    private final ModelMapper modelMapper;

    public ActivityController(ActivityService activityService, ModelMapper modelMapper) {
        this.service = activityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<ActivityDTO> getAllActivities() {
        return service.findAllActivities().stream()
                .map(activity -> modelMapper.map(activity, ActivityDTO.class)) // Entité -> DTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable long id) {
        Activity activity = service.findActivityById(id);
        ActivityDTO responseDTO = modelMapper.map(activity, ActivityDTO.class); // Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/cv/{cvId}")
    public ResponseEntity<ActivityDTO> createActivity(@RequestBody ActivityDTO activityDTO, @PathVariable long cvId) {
        Activity activityRequest = modelMapper.map(activityDTO, Activity.class); // DTO -> Entité
        Activity activity = service.createActivity(cvId, activityRequest);
        ActivityDTO responseDTO = modelMapper.map(activity, ActivityDTO.class); // Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDTO> updateActivity(@PathVariable long id, @RequestBody ActivityDTO activityDTO) {
        Activity activityRequest = modelMapper.map(activityDTO, Activity.class); // DTO -> Entité
        Activity activity = service.updateActivity(id, activityRequest);
        ActivityDTO responseDTO = modelMapper.map(activity, ActivityDTO.class); // Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable long id) {
        service.deleteActivityById(id);
        return ResponseEntity.noContent().build();
    }
}