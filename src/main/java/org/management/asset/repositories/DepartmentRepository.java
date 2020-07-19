package org.management.asset.repositories;

import org.management.asset.entities.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository(value = "departementMongoRepository")
@RepositoryRestResource
public interface DepartmentRepository extends MongoRepository<Department, String> {

    Optional<Department> findByNameIgnoreCase(final String name);

}
