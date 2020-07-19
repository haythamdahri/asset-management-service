package org.management.asset.dao;

import org.management.asset.bo.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface LocationRepository extends MongoRepository<Location, String> {

    Optional<Location> findByNameIgnoreCase(final String name);

}
