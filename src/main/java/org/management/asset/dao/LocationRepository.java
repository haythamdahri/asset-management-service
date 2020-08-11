package org.management.asset.dao;

import org.management.asset.bo.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface LocationRepository extends MongoRepository<Location, String> {

    Optional<Location> findByNameIgnoreCase(final String name);

    @Query(value = "{$or: [{'city': {$regex : '.*?0.*', $options: 'i'}}, {'state': {$regex : '.*?0.*', $options: 'i'}}, " +
            "{'zip': {$regex : '.*?0.*', $options: 'i'}}, {'address1': {$regex : '.*?0.*', $options: 'i'}}, {'address2': {$regex : '.*?0.*', $options: 'i'}}, " +
            "{'country': {$regex : '.*?0.*', $options: 'i'}}, {'name': {$regex : '.*?0.*', $options: 'i'}}]}")
    Page<Location> searchLocation(final String search, @PageableDefault Pageable pageable);

}
