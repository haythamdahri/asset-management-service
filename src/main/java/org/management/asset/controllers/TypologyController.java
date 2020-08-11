package org.management.asset.controllers;

import org.management.asset.bo.*;
import org.management.asset.dto.*;
import org.management.asset.services.TypologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/typologies")
public class TypologyController {

    @Autowired
    private TypologyService typologyService;

    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getTypologiesCounter() {
        return ResponseEntity.ok(this.typologyService.getTypologiesCounter());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TYPOLOGIES_EDIT') or hasRole('ROLE_TYPOLOGIES_CREATE') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<Typology> saveTypology(@RequestBody TypologyRequestDTO typologyRequest) {
        return ResponseEntity.ok(this.typologyService.saveTypology(typologyRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TYPOLOGIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<Page<Typology>> getTypologiesPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.typologyService.getTypologies(name, page, size, direction, sort));
    }

    @GetMapping(path = "/custom")
    public ResponseEntity<List<TypologyResponseDTO>> getCustomTypologies() {
        return ResponseEntity.ok(this.typologyService.getCustomTypologies());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TYPOLOGIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Typology> getTypology(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(this.typologyService.getTypology(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TYPOLOGIES_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Typology> deleteTypology(@PathVariable(name = "id") String id) {
        this.typologyService.deleteTypology(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{typologyId}/threats/{threatId}/status")
    public ResponseEntity<Threat> updateTypologyThreat(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "threatId") String threatId, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.typologyService.updateTypologyThreatStatus(typologyId, threatId, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{typologyId}/threats/{threatId}")
    public ResponseEntity<ThreatResponseDTO> getTypologyThreat(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "threatId") String threatId) {
        return ResponseEntity.ok(this.typologyService.getTypologyThreat(typologyId, threatId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THREATS_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{typologyId}/threats/{threatId}")
    public ResponseEntity<Void> deleteTypologyThreat(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "threatId") String threatId) {
        this.typologyService.deleteTypologyThreat(typologyId, threatId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VULNERABILITIES_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{typologyId}/vulnerabilities/{vulnerabilityId}")
    public ResponseEntity<Void> deleteTypologyVulnerability(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "vulnerabilityId") String vulnerabilityId) {
        this.typologyService.deleteTypologyVulnerability(typologyId, vulnerabilityId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VULNERABILITIES_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{typologyId}/vulnerabilities/{vulnerabilityId}/status")
    public ResponseEntity<Vulnerability> updateTypologyVulnerabilityStatus(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "vulnerabilityId") String vulnerabilityId, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.typologyService.updateTypologyVulnerabilityStatus(typologyId, vulnerabilityId, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VULNERABILITIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{typologyId}/vulnerabilities/{vulnerabilityId}")
    public ResponseEntity<VulnerabilityResponseDTO> getTypologyVulnerability(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "vulnerabilityId") String vulnerabilityId) {
        return ResponseEntity.ok(this.typologyService.getTypologyVulnerability(typologyId, vulnerabilityId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{typologyId}/riskscenarios/{riskScenarioId}/status")
    public ResponseEntity<RiskScenario> updateTypologyRiskScenarioStatus(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "riskScenarioId") String riskScenarioId, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.typologyService.updateTypologyRiskScenarioStatus(typologyId, riskScenarioId, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{typologyId}/riskscenarios/{riskScenarioId}")
    public ResponseEntity<RiskScenarioResponseDTO> getTypologyRiskScenario(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "riskScenarioId") String riskScenarioId) {
        return ResponseEntity.ok(this.typologyService.getTypologyRiskScenario(typologyId, riskScenarioId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{typologyId}/riskscenarios/{riskScenarioId}")
    public ResponseEntity<Void> deleteTypologyRiskScenario(@PathVariable(name = "typologyId") String typologyId, @PathVariable(name = "riskScenarioId") String riskScenarioId) {
        this.typologyService.deleteTypologyRiskScenario(typologyId, riskScenarioId);
        return ResponseEntity.ok().build();
    }

}
