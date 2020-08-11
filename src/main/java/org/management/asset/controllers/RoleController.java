package org.management.asset.controllers;

import org.management.asset.bo.Role;
import org.management.asset.services.RoleService;
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
@RequestMapping(path = "/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<List<Role>> listRoles() {
        return ResponseEntity.ok(this.roleService.getRoles());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getRolesCounter() {
        return ResponseEntity.ok(this.roleService.getRolesCounter());
    }

}
