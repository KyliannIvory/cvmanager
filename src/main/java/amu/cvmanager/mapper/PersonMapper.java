package amu.cvmanager.mapper;

import amu.cvmanager.dto.PersonDTO;
import amu.cvmanager.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    private CVMapper cvMapper;

    public PersonMapper(CVMapper cvMapper) {
        this.cvMapper = cvMapper;
    }

    public Person fromDTO(PersonDTO personDTO) {
        return  new Person(
                personDTO.id(),
                personDTO.lastName(),
                personDTO.firstName(),
                personDTO.email(),
                personDTO.website(),
                personDTO.password(),
                personDTO.birthDate(),
                cvMapper.fromDTO(personDTO.cv())
        );
    }

    public PersonDTO toDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getWebsite(),
                person.getPassword(),
                person.getBirthDate(),
                cvMapper.toDTO(person.getCv())
        );
    }

}
