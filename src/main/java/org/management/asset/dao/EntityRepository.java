package org.management.asset.dao;

import org.management.asset.bo.Entity;
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
public interface EntityRepository extends MongoRepository<Entity, String> {

    Optional<Entity> findByNameIgnoreCase(final String name);

    Page<Entity> findByNameContainingIgnoreCase(final String name, @PageableDefault Pageable pageable);

}
