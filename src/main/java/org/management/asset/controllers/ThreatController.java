package org.management.asset.controllers;

import org.management.asset.bo.Threat;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.ThreatRequestDTO;
import org.management.asset.dto.ThreatResponseDTO;
import org.management.asset.services.ThreatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/threats")
public class ThreatController {

    @Autowired
    private ThreatService threatService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getThreatsCounter() {
        return ResponseEntity.ok(this.threatService.getThreatsCounter());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<List<Threat>> getAllThreats() {
        return ResponseEntity.ok(this.threatService.getThreats());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<PageDTO<ThreatResponseDTO>> getThreatsPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.threatService.getThreats(name, page, size, direction, sort));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_EDIT') or hasRole('ROLE_THREATS_CREATE') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<ThreatResponseDTO> getTypologyThreat(@RequestBody ThreatRequestDTO threatRequest) {
        return ResponseEntity.ok(this.threatService.saveThreat(threatRequest));
    }

}
