package amu.cvmanager.repository;

import amu.cvmanager.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 http://localhost:8080/cVs
 **/
@Repository
@Transactional
public interface CVRepository extends JpaRepository<CV,Long> {

    @Query("""
        SELECT DISTINCT c
        FROM CV c
        LEFT JOIN c.person p
        LEFT JOIN c.activities a
        WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(CONCAT(p.lastName, ' ', p.firstName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """)
    List<CV> searchCVs(String searchTerm);}
