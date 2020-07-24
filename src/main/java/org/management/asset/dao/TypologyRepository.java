package org.management.asset.dao;

import org.management.asset.bo.Typology;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface TypologyRepository extends MongoRepository<Typology, String> {
}