package amu.cvmanager.repository;

import amu.cvmanager.model.CV;
import org.springframework.data.repository.CrudRepository;

/**
 http://localhost:8080/cVs
 **/

public interface CVRepository extends CrudRepository<CV,Long> {
}
