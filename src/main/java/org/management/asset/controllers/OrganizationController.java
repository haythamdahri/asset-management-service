package org.management.asset.controllers;

import org.management.asset.bo.AssetFile;
import org.management.asset.bo.Organization;
import org.management.asset.dto.OrganizationRequestDTO;
import org.management.asset.services.OrganizationService;
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
@RequestMapping(path = "/api/v1/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Organization>> getOrganizations() {
        return ResponseEntity.ok(this.organizationService.getOrganizations());
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_ORGANIZATIONS_CREATE') or hasRole('ROLE_ORGANIZATIONS_UPDATE') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/")
    public ResponseEntity<Organization> saveOrganization(@ModelAttribute OrganizationRequestDTO organizationRequest) {
        return ResponseEntity.ok(this.organizationService.saveOrganization(organizationRequest));
    }

    @GetMapping(path = "/counter")
    public ResponseEntity<Long> countOrganizations() {
        return ResponseEntity.ok(this.organizationService.getOrganizationsCounter());
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_ORGANIZATIONS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<Page<Organization>> listOrganizations(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.organizationService.getOrganizations(search, page, size));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_ORGANIZATIONS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable(name = "id") String organizationId) {
        return ResponseEntity.ok(this.organizationService.getOrganization(organizationId));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_ORGANIZATIONS_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable(name = "id") String organizationId) {
        this.organizationService.deleteOrganization(organizationId);
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/{id}/image/file")
    public ResponseEntity<byte[]> getOrganizationAvatarFile(@PathVariable(name = "id") String organizationId) {
        // Retrieve AssetFile
        AssetFile assetFile = this.organizationService.getOrganizationImage(organizationId);
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
