package org.management.asset.controllers;

import org.management.asset.bo.RiskScenario;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskScenarioRequestDTO;
import org.management.asset.dto.RiskScenarioResponseDTO;
import org.management.asset.services.RiskScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/riskscenarios")
public class RiskScenarioController {

    @Autowired
    private RiskScenarioService riskScenarioService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<List<RiskScenario>> getAllRiskScenarios() {
        return ResponseEntity.ok(this.riskScenarioService.getRiskScenarios());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/counter")
    public ResponseEntity<Long> getRiskScenariosCounter() {
        return ResponseEntity.ok(this.riskScenarioService.getRiskScenariosCounter());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<PageDTO<RiskScenarioResponseDTO>> getRiskScenariosPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.riskScenarioService.getRiskScenarios(name, page, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKSCENARIOS_EDIT') or hasRole('ROLE_THREATS_CREATE') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<RiskScenarioResponseDTO> saveTypologyRiskScenario(@RequestBody RiskScenarioRequestDTO riskScenarioRequest) {
        return ResponseEntity.ok(this.riskScenarioService.saveRiskScenario(riskScenarioRequest));
    }

}
