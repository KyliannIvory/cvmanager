package amu.cvmanager.repository;

import amu.cvmanager.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByEmail(String email);

    /**
     * Recherche de personnes dont le nom, le prénom ou le titre d'une activité
     * correspond au terme de recherche (insensible à la casse).
     */
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN p.cv cv LEFT JOIN cv.activities a " +
            "WHERE LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Person> searchPersons(@Param("searchTerm") String searchTerm);
}
