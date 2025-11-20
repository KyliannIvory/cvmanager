package amu.cvmanager.controller;

import amu.cvmanager.dto.CVDTO;
import amu.cvmanager.model.CV;
import amu.cvmanager.service.CVService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cvmanager/cv")
public class CVController {

    private final CVService service;
    private final ModelMapper modelMapper;

    public CVController(CVService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<CVDTO> createCV(@RequestBody CVDTO cvDTO) {
        CV cvRequest = modelMapper.map(cvDTO, CV.class); // DTO -> Entité
        CV cv = service.createCV(cvRequest);
        CVDTO responseDTO = modelMapper.map(cv, CVDTO.class); // Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping()
    public List<CVDTO> findAllCVs() {
        return service.findAllCVs().stream()
                .map(cv -> modelMapper.map(cv, CVDTO.class)) // Entité -> DTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CVDTO> findCVById(@PathVariable long id) {
        CV cv = service.findCVById(id);
        CVDTO responseDTO = modelMapper.map(cv, CVDTO.class); // Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CVDTO> updateCV(@PathVariable long id, @RequestBody CVDTO cvDTO) {
        CV cvRequest = modelMapper.map(cvDTO, CV.class); // DTO -> Entité
        CV cv = service.updateCV(id, cvRequest);
        CVDTO responseDTO = modelMapper.map(cv, CVDTO.class); // Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable long id) {
        service.deleteCVById(id);
        return ResponseEntity.noContent().build();
    }
}