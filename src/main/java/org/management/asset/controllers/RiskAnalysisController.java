package org.management.asset.controllers;

import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskAnalysisRequestDTO;
import org.management.asset.dto.RiskAnalysisResponseDTO;
import org.management.asset.services.RiskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/riskanalyzes")
public class RiskAnalysisController {

    @Autowired
    private RiskAnalysisService riskAnalysisService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKANALYZES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getRiskAnalyzesCounter() {
        return ResponseEntity.ok(this.riskAnalysisService.getRiskAnalyzesCounter());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKANALYZES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<PageDTO<RiskAnalysisResponseDTO>> getRiskAnalyzesPage(@RequestParam(value = "assetId", required = false, defaultValue = "") String assetId, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.riskAnalysisService.getRiskAnalyzes(assetId, page, size, direction, sort));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RISKANALYZES_EDIT') or hasRole('ROLE_RISKANALYZES_CREATE') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<RiskAnalysisResponseDTO> saveRiskAnalysis(@RequestBody RiskAnalysisRequestDTO riskAnalysisRequest) {
        return ResponseEntity.ok(this.riskAnalysisService.saveRiskAnalysis(riskAnalysisRequest));
    }

}
