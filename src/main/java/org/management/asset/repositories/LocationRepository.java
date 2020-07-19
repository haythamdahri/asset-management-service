package org.management.asset.repositories;

import org.management.asset.entities.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository(value = "locationMongoRepository")
@RepositoryRestResource
public interface LocationRepository extends MongoRepository<Location, String> {

    Optional<Location> findByNameIgnoreCase(final String name);

}
