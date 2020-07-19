package org.management.asset.dao;

import org.management.asset.bo.Role;
import org.management.asset.bo.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRoleName(final RoleType roleType);

}
