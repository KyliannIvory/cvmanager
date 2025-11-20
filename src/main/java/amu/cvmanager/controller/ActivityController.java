package amu.cvmanager.controller;

import amu.cvmanager.model.Activity;
import amu.cvmanager.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cvmanager/activities")
public class ActivityController {

    private final ActivityService service;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.service = activityService;
    }

    @GetMapping()
    public List<Activity> getAllActivities() {
        return service.findAllActivities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable long id) {
        return ResponseEntity.ok(service.findActivityById(id));
    }

    @PostMapping("/cv/{cvId}")
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity, @PathVariable long cvId) {
        return ResponseEntity.ok(service.createActivity(cvId, activity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable long id, @RequestBody Activity activity) {
        return ResponseEntity.ok(service.updateActivity(id, activity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable long id) {
        service.deleteActivityById(id);
        return ResponseEntity.noContent().build();
    }
}
