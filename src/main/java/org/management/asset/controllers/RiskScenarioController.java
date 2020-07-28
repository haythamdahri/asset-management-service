package org.management.asset.controllers;

import org.management.asset.bo.Vulnerability;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.VulnerabilityRequestDTO;
import org.management.asset.dto.VulnerabilityResponseDTO;
import org.management.asset.services.VulnerabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/vulnerabilities")
public class VulnerabilityController {

    @Autowired
    private VulnerabilityService vulnerabilityService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VULNERABILITIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<List<Vulnerability>> getAllVulnerabilities() {
        return ResponseEntity.ok(this.vulnerabilityService.getVulnerabilities());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VULNERABILITIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<PageDTO<VulnerabilityResponseDTO>> getVulnerabilitiesPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.vulnerabilityService.getVulnerabilities(name, page, size));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VULNERABILITIES_EDIT') or hasRole('ROLE_THREATS_CREATE') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<VulnerabilityResponseDTO> getTypologyThreat(@RequestBody VulnerabilityRequestDTO vulnerabilityRequest) {
        return ResponseEntity.ok(this.vulnerabilityService.saveVulnerability(vulnerabilityRequest));
    }

}
