package org.management.asset.dao;

import org.management.asset.bo.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByNameIgnoreCase(@Param("name") String name);

}
