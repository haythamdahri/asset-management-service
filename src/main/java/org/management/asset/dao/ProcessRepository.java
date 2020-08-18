package org.management.asset.dao;

import org.management.asset.bo.Asset;
import org.management.asset.bo.Process;
import org.management.asset.dto.ProcessDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    Optional<Process> findProcessByNameAndOrganization_Id(final String name, final String id);

    @Query(value = "{}", fields = "{id: 1, name: 1, organization: 1}")
    List<ProcessDTO> findCustomProcesses();

    @Query(value = "{id: {$ne: ?0}}", fields = "{id: 1, name: 1, organization: 1}")
    List<ProcessDTO> findCustomProcessesAndExclude(String excludedProcessId);

}
