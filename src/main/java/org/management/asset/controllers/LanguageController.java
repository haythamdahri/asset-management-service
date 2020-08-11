package org.management.asset.controllers;

import org.management.asset.bo.Language;
import org.management.asset.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/languages")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getLanguagesCounter() {
        return ResponseEntity.ok(this.languageService.getLanguagesCounter());
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<Language>> listLanguages() {
        return ResponseEntity.ok(this.languageService.getLanguages());
    }

}
