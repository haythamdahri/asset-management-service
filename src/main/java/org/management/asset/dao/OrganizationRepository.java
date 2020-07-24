package org.management.asset.dao;

import org.management.asset.bo.Organization;
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
public interface OrganizationRepository extends MongoRepository<Organization, String> {

    Optional<Organization> findByNameIgnoreCase(final String name);

    @Query(value = "{}", count = true)
    Long countOrganizations();

    Page<Organization> findByNameContainingIgnoreCase(final String name, @PageableDefault Pageable pageable);

}
