package org.management.asset.controllers;

import org.management.asset.bo.Company;
import org.management.asset.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path =  "/api/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Company>> listCompanies() {
        return ResponseEntity.ok(this.companyService.getCompanies());
    }

}
