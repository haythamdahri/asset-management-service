package org.management.asset.controllers;

import org.management.asset.bo.Organization;
import org.management.asset.services.OrganizationService;
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
@RequestMapping(path = "/api/v1/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Organization>> listCompanies() {
        return ResponseEntity.ok(this.organizationService.getOrganizations());
    }

}
