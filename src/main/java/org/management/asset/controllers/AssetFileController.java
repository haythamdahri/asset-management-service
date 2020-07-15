package org.management.asset.controllers;

import org.management.asset.bo.AssetFile;
import org.management.asset.services.AssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path  = "/api/v1/assetfiles")
public class AssetFileController {

    @Autowired
    private AssetFileService assetFileService;

    /**
     * Retrieve Blob file only
     *
     * @param id: RestaurantFile Identifier
     * @return ResponseEntity<byte [ ]>
     */
    @GetMapping(path = "/{id}/file")
    public ResponseEntity<byte[]> getAssetFileBytes(@PathVariable(value = "id") Long id) {
        // Retrieve AssetFile
        AssetFile assetFile = this.assetFileService.getAssetFile(id);
        // Check file existence
        if (assetFile != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(assetFile.getMediaType()));
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(assetFile.getFile());
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }


}
