package org.management.asset.dao;

import org.management.asset.bo.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface AssetRepository extends MongoRepository<Asset, String> {

    @Query(value = "{}", count = true)
    Long countAssets();

}
