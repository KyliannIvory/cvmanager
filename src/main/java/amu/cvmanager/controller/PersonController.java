package amu.cvmanager.controller;


import amu.cvmanager.dto.PersonDTO;
import amu.cvmanager.model.Person;
import amu.cvmanager.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cvmanager/persons")
public class PersonController {

    private final PersonService service;
    private final ModelMapper modelMapper;

    // Mise à jour pour inclure ModelMapper dans le constructeur
    public PersonController(PersonService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<PersonDTO> findAllPersons() {
        return service.findAllpersons().stream()
                .map(person -> modelMapper.map(person, PersonDTO.class)) // Mapping Entité -> DTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> findPersonById(@PathVariable long id) {
        Person person = service.findPersonById(id);
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class); // Mapping Entité -> DTO
        return ResponseEntity.ok(personDTO);
    }

    @PostMapping()
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        Person personRequest = modelMapper.map(personDTO, Person.class); // Mapping DTO -> Entité
        Person person = service.createPerson(personRequest);
        PersonDTO responseDTO = modelMapper.map(person, PersonDTO.class); // Mapping Entité -> DTO
        System.out.println("Person created: " + person.getLastName()+person.getFirstName());
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable long id, @RequestBody PersonDTO personDTO) {
        Person personRequest = modelMapper.map(personDTO, Person.class); // Mapping DTO -> Entité
        Person person = service.updatePerson(id, personRequest);
        PersonDTO responseDTO = modelMapper.map(person, PersonDTO.class); // Mapping Entité -> DTO
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable long id) {
        service.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public List<PersonDTO> searchPersons(@RequestParam("q") String query) {
        return service.searchPersons(query).stream()
                .map(person -> modelMapper.map(person, PersonDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonDTO> findPersonByEmail(@PathVariable String email) {
        Person person = service.findByEmail(email);
        PersonDTO dto = modelMapper.map(person, PersonDTO.class);
        return ResponseEntity.ok(dto);
    }

}