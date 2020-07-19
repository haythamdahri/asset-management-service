package org.management.asset.repositories;

import org.management.asset.entities.Role;
import org.management.asset.entities.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository(value = "roleMongoRepository")
@RepositoryRestResource
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRoleName(final RoleType roleType);

}
