package org.management.asset.dao;

import org.management.asset.bo.Process;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

/**
 * @author Haytham DAHRI
 * Object d'accés aux données des processus
 */
@Repository
@RepositoryRestResource
public interface ProcessRepository extends MongoRepository<Process, String> {

    @Query(value = "{}", count = true)
    Long countProcesses();

    Page<Process> findByNameContainingIgnoreCase(final String name, @PageableDefault Pageable pageable);

}
