package org.management.asset.dao;

import org.management.asset.bo.Typology;
import org.management.asset.dto.TypologyResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface TypologyRepository extends MongoRepository<Typology, String> {

    Page<Typology> findByNameContainingIgnoreCase(final String name, @PageableDefault Pageable pageable);

    Optional<Typology> findByName(final String name);

    @Query(value = "{}", fields = "{id: 1, name: 1}")
    List<TypologyResponseDTO> findCustomTypologies();

}
