package org.management.asset.dao;

import org.management.asset.bo.Role;
import org.management.asset.bo.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(@Param("roleName") RoleType roleType);

}
