package org.management.asset.dao;

import org.management.asset.bo.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByNameIgnoreCase(@Param("name") String name);

}
