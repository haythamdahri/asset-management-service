package org.management.asset.repositories;

import org.management.asset.entities.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository(value = "companyMongoRepository")
@RepositoryRestResource
public interface CompanyRepository extends MongoRepository<Company, String> {

    Optional<Company> findByNameIgnoreCase(final String name);

}
