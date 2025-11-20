package amu.cvmanager.controller;


import amu.cvmanager.model.Person;
import amu.cvmanager.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cvmanager/persons")
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service){
        this.service = service;
    }

    @GetMapping()
    public List<Person> findAllPersons() {
        return service.findAllpersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable long id) {
        return ResponseEntity.ok(service.findPersonById(id));
    }

    @PostMapping()
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return ResponseEntity.ok(service.createPerson(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable long id, @RequestBody Person person) {
        return ResponseEntity.ok(service.updatePerson(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable long id) {
        service.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }
}
