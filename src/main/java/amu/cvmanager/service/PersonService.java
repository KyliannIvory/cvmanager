package amu.cvmanager.service;

import amu.cvmanager.exception.PersonNotFoundException;
import amu.cvmanager.model.Person;
import amu.cvmanager.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder; // AJOUTÉ


    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person createPerson(Person person) {

        // CRYPTAGE DU MOT DE PASSE LORS DE LA CRÉATION
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        if (person.getCv() != null) {
            person.assignCV(person.getCv());
        }

        return personRepository.save(person);
    }

    public Person findPersonById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
    }
    public Optional<Person> findPersonByEmail(String email) {
        return personRepository.findByEmail(email);
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
    public List<Person> searchPersons(String searchTerm) {
        // Optionnel : si le terme est vide, nous déléguons au Repository de retourner tous ou une liste vide.
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return personRepository.findAll();
        }
        return personRepository.searchPersons(searchTerm);
    }

}
