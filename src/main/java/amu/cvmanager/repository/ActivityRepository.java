package amu.cvmanager.repository;

import org.springframework.data.repository.CrudRepository;
import amu.cvmanager.model.Activity;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "activities", path = "activities")
public interface ActivityRepository extends CrudRepository<Activity,Long> {
}
