package org.management.asset.controllers;

import org.management.asset.dto.RecaptchaResponseDTO;
import org.management.asset.services.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping(path = "/verify")
    public ResponseEntity<RecaptchaResponseDTO> verifyCaptcha(@RequestParam(name = "humanKey") String humanKey) {
        return ResponseEntity.ok(this.captchaService.verifyCaptcha(humanKey));
    }

}
