package amu.cvmanager.controller;

import amu.cvmanager.model.CV;
import amu.cvmanager.service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cvmanager/cv")
public class CVController {

    private final CVService service;

    @Autowired
    public CVController(CVService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CV> createCV(@RequestBody CV cv) {
        return ResponseEntity.ok(service.createCV(cv));
    }

    @GetMapping()
    public List<CV> findAllCVs() {
        return service.findAllCVs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CV> findCVById(@PathVariable long id) {
        return ResponseEntity.ok(service.findCVById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable long id) {
        service.deleteCVById(id);
        return ResponseEntity.noContent().build();
    }
}
