package org.management.asset.dao;

import org.management.asset.bo.Asset;
import org.management.asset.dto.AssetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
public interface AssetRepository extends MongoRepository<Asset, String> {

    @Query(value = "{}", count = true)
    Long countAssets();

    List<Asset> findAssetsByTypology_Id(final String id);

    Page<Asset> findByNameContainingIgnoreCase(final String name, @PageableDefault Pageable pageable);

    @Query(value = "{}", fields="{id: 1, name: 1}")
    List<AssetDTO> findCustomAssets();

}
