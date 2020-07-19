package org.management.asset.repositories;

import org.management.asset.entities.Language;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository(value = "languageMongoRepository")
@RepositoryRestResource
public interface LanguageRepository extends MongoRepository<Language, String> {

    Optional<Language> findByNameIgnoreCase(final String name);

}
