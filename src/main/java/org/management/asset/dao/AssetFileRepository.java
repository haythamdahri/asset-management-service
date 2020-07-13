package org.management.asset.dao;

import org.management.asset.bo.AssetFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface AssetFileRepository extends JpaRepository<AssetFile, Long> {

    Page<AssetFile> findByOrderByIdDesc(@PageableDefault Pageable pageable);

    Optional<AssetFile> findByNameContainingIgnoreCase(String name);

    Page<AssetFile> findByNameContainingIgnoreCaseOrId(@PageableDefault Pageable pageable, String name, String id);

}
