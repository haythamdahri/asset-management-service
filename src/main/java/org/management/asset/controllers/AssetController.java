package org.management.asset.controllers;

import org.management.asset.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

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

}
