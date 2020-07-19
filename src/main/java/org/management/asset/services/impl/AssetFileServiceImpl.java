package org.management.asset.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.management.asset.bo.AssetFile;
import org.management.asset.dao.AssetFileRepository;
import org.management.asset.services.AssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Haytham DAHRI
 */
@Service
public class AssetFileServiceImpl implements AssetFileService {

    @Autowired
    private AssetFileRepository assetFileRepository;

    @Override
    public AssetFile saveAssetFile(AssetFile file) {
        return this.assetFileRepository.save(file);
    }

    @Override
    public AssetFile saveAssetFile(MultipartFile file) throws IOException {
        AssetFile restaurantFile = new AssetFile(null, FilenameUtils.removeExtension(file.getOriginalFilename()), FilenameUtils.getExtension(file.getOriginalFilename()), file.getContentType(), file.getBytes(), null);
        return this.assetFileRepository.save(restaurantFile);
    }

    @Override
    public AssetFile saveAssetFile(MultipartFile file, AssetFile assetFile) throws IOException {
        // Check if assetFile is null
        if( assetFile == null ){
            assetFile = new AssetFile();
        }
        // Update image data
        assetFile.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
        assetFile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
        assetFile.setFile(file.getBytes());
        assetFile.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).toString());
        return this.assetFileRepository.save(assetFile);
    }

    @Override
    public Boolean deleteAssetFile(Long id) {
        this.assetFileRepository.deleteById(id);
        return true;
    }

    @Override
    public AssetFile getAssetFile(Long id) {
        return this.assetFileRepository.findById(id).orElse(null);
    }


    @Override
    public AssetFile getAssetFileByUser(Long userId) {
        return this.assetFileRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public AssetFile getAssetFile(String name) {
        return this.assetFileRepository.findByNameContainingIgnoreCase(name).orElse(null);
    }

    @Override
    public Page<AssetFile> getAssetFiles(int page, int size) {
        return this.assetFileRepository.findByOrderByIdDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<AssetFile> searchAssetFiles(int page, int size, String search) {
        return this.assetFileRepository.findByNameContainingIgnoreCaseOrId(PageRequest.of(page, size, Sort.Direction.DESC, "id"), search, search);
    }
    @Override
    public List<AssetFile> getAssetFiles() {
        return this.assetFileRepository.findAll();
    }
}
