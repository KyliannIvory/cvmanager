package amu.cvmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import amu.cvmanager.model.Activity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Optional<Activity> findByTitleLike(String name);
}
