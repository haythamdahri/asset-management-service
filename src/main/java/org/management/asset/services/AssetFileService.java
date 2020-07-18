package org.management.asset.services;

import org.management.asset.bo.AssetFile;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface AssetFileService {


    AssetFile saveAssetFile(AssetFile file);

    AssetFile saveAssetFile(MultipartFile file) throws IOException;

    AssetFile saveAssetFile(MultipartFile file, AssetFile restaurantFile) throws IOException;

    Boolean deleteAssetFile(Long id);

    AssetFile getAssetFile(Long id);

    AssetFile getAssetFileByUser(Long userId);

    AssetFile getAssetFile(String name);

    Page<AssetFile> getAssetFiles(int page, int size);

    Page<AssetFile> searchAssetFiles(int page, int size, String search);

    List<AssetFile> getAssetFiles();

}
