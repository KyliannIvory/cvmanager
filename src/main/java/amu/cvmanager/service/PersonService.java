package amu.cvmanager.service;

import amu.cvmanager.exception.PersonNotFoundException;
import amu.cvmanager.model.Person;
import amu.cvmanager.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;


    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public Person createPerson(Person person) {

        if (person.getCv() != null) {
            person.assignCV(person.getCv());
        }

        return personRepository.save(person);
    }

    public Person findPersonById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
    }

    public List<Person> findAllpersons(){
        return personRepository.findAll();
    }


    public Person updatePerson(long id, Person updated) {
        Person existing = findPersonById(id);

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setWebsite(updated.getWebsite());
        existing.setBirthDate(updated.getBirthDate());
        existing.setPassword(updated.getPassword());

        if (updated.getCv() != null) {
            existing.assignCV(updated.getCv());
        }

        return personRepository.save(existing);
    }

    public void deletePersonById(long id) {
        Person person = findPersonById(id);
        personRepository.delete(person);
    }

}
