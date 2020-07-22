package org.management.asset.dao;

import org.management.asset.bo.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface GroupRepository extends MongoRepository<Group, String> {

    Optional<Group> findByNameIgnoreCase(final String name);

    Page<Group> findByNameContainingIgnoreCase(final String name, @PageableDefault Pageable pageable);

}
