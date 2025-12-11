package amu.cvmanager.controller;

import amu.cvmanager.dto.CVDTO;
import amu.cvmanager.dto.CVDTOv2;
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
        // ... (Logique de création)
        CV cvRequest = modelMapper.map(cvDTO, CV.class);
        CV cv = service.createCV(cvRequest);
        CVDTO responseDTO = modelMapper.map(cv, CVDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    //test avec CVSearchedDTO
    @GetMapping()
    public List<CVDTOv2> findAllCVs() {
        return service.findAllCVs().stream()
                .map(cv -> modelMapper.map(cv, CVDTOv2.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CVDTOv2> findCVById(@PathVariable long id) {
        CV cv = service.findCVById(id);
        CVDTOv2 responseDTO = modelMapper.map(cv, CVDTOv2.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CVDTOv2> updateCV(@PathVariable long id, @RequestBody CVDTOv2 cvDTO) {
        // Le service gère la vérification de propriété avant d'exécuter la mise à jour
        CV cvRequest = modelMapper.map(cvDTO, CV.class);
        CV cv = service.updateCV(id, cvRequest);
        CVDTOv2 responseDTO = modelMapper.map(cv, CVDTOv2.class);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable long id) {
        // Le service gère la vérification de propriété avant d'exécuter la suppression
        service.deleteCVById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<CVDTOv2> findCVsByName(@RequestParam("q") String query) {
        return service.searchCVs(query).stream()
                .map(cv -> modelMapper.map(cv, CVDTOv2.class))
                .collect(Collectors.toList());
    }
}