package amu.cvmanager.repository;

import amu.cvmanager.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 http://localhost:8080/cVs
 **/
@Repository
@Transactional
public interface CVRepository extends JpaRepository<CV,Long> {
}
