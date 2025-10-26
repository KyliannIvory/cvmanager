package amu.cvmanager.repository;

import amu.cvmanager.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,Long> {
}
