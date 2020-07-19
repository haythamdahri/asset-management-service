package org.management.asset.repositories;

import org.management.asset.entities.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository(value = "groupMongoRepository")
@RepositoryRestResource
public interface GroupRepository extends MongoRepository<Group, String> {

    Optional<Group> findByNameIgnoreCase(final String name);

}
