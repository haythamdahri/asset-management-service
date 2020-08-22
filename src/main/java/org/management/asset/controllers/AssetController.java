package org.management.asset.controllers;

import org.management.asset.bo.Asset;
import org.management.asset.bo.AssetFile;
import org.management.asset.bo.Process;
import org.management.asset.bo.RiskAnalysis;
import org.management.asset.bo.User;
import org.management.asset.dto.AssetDTO;
import org.management.asset.dto.AssetRequestDTO;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskAnalysisResponseDTO;
import org.management.asset.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSETS_CREATE') or hasRole('ROLE_ASSETS_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Asset> saveAsset(@ModelAttribute AssetRequestDTO assetRequest) {
        return ResponseEntity.ok(this.assetService.saveAsset(assetRequest));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('SUPER_USER')")
    @GetMapping(path = "/counter")
    public ResponseEntity<Long> countAssets() {
        try {
            return ResponseEntity.ok(this.assetService.getAssetsCounter());
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<AssetDTO>> getAssets() {
        return ResponseEntity.ok(this.assetService.getCustomAssets());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSETS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<Page<Asset>> getAssetsPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.assetService.getAssets(name, page, size, direction, sort));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSETS_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{id}/status")
    public ResponseEntity<Asset> updateAssetStatus(@PathVariable(name = "id") String id, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.assetService.updateAssetStatus(id, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSETS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Asset> getAsset(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(this.assetService.getAsset(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSETS_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable(name = "id") String id) {
        this.assetService.deleteAsset(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}/image/file")
    public ResponseEntity<byte[]> getAssetImageFile(@PathVariable(name = "id") String id) {
        // Retrieve AssetFile
        Asset asset = this.assetService.getAsset(id);
        // Check file existence
        if (asset != null && asset.getImage() != null) {
            AssetFile image = asset.getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(image.getMediaType()));
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getFile());
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKANALYZES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{assetId}/riskanalyzes/{riskAnalysisId}")
    public ResponseEntity<RiskAnalysisResponseDTO> getAssetRiskAnalysis(@PathVariable(name = "assetId") String assetId, @PathVariable(name = "riskAnalysisId") String riskAnalysisId) {
        return ResponseEntity.ok(this.assetService.getAssetRiskAnalysis(assetId, riskAnalysisId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKANALYZES_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{assetId}/riskanalyzes/{riskAnalysisId}/status")
    public ResponseEntity<RiskAnalysis> updateAssetRiskAnalysisStatus(@PathVariable(name = "assetId") String assetId, @PathVariable(name = "riskAnalysisId") String riskAnalysisId, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.assetService.updateAssetRiskAnalysisStatus(assetId, riskAnalysisId, status));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSET_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{id}/classification/status")
    public ResponseEntity<Asset> updateProcessClassificationStatus(@PathVariable(name = "id") String id, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.assetService.updateAssetClassificationStatus(id, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKANALYZES_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{assetId}/riskanalyzes/{riskAnalysisId}")
    public ResponseEntity<Void> deleteAssetRiskAnalysis(@PathVariable(name = "assetId") String assetId, @PathVariable(name = "riskAnalysisId") String riskAnalysisId) {
        this.assetService.deleteAssetRiskAnalysis(assetId, riskAnalysisId);
        return ResponseEntity.ok().build();
    }

}
