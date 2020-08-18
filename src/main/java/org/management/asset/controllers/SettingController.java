package org.management.asset.controllers;

import org.management.asset.bo.Setting;
import org.management.asset.dto.AnalysisOptionsResponseDTO;
import org.management.asset.dto.ClassificationResponseDTO;
import org.management.asset.dto.SettingRequestDTO;
import org.management.asset.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/active")
    public ResponseEntity<Setting> getActiveSetting() {
        return ResponseEntity.ok(this.settingService.getActiveSetting());
    }

    @GetMapping(path = "/active/classification")
    public ResponseEntity<ClassificationResponseDTO> getActiveSettingClassification() {
        return ResponseEntity.ok(this.settingService.getActiveSettingClassification());
    }

    @GetMapping(path = "/active/riskanalyzes")
    public ResponseEntity<AnalysisOptionsResponseDTO> getActiveSettingRiskAnalysisOptions() {
        return ResponseEntity.ok(this.settingService.getActiveSettingRiskAnalysisOptions());
    }

    @GetMapping(path = "/active/captcha")
    public ResponseEntity<Integer> getActiveSettingMaxAttemptsWithoutCaptcha() {
        return ResponseEntity.ok(this.settingService.getActiveSetting().getMaxAttemptsWithoutCaptcha());
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<Setting> saveSetting(@RequestBody SettingRequestDTO settingRequest) {
        return ResponseEntity.ok(this.settingService.saveSetting(settingRequest));
    }

}
