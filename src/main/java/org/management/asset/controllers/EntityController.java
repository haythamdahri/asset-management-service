package org.management.asset.controllers;

import org.management.asset.bo.Entity;
import org.management.asset.dto.EntityRequestDTO;
import org.management.asset.services.EntityService;
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
@RequestMapping(path = "/api/v1/entities")
public class EntityController {

    @Autowired
    private EntityService entityService;


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENTITIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<List<Entity>> listEntities() {
        return ResponseEntity.ok(this.entityService.getEntities());
    }

    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getEntitiesCounter() {
        return ResponseEntity.ok(this.entityService.getEntitiesCounter());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENTITIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<Page<Entity>> getEntitiesPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.entityService.getEntities(name, page, size, direction, sort));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENTITIES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Entity> getEntity(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(this.entityService.getEntity(id));
    }

    @GetMapping(path = "/organizations/{organizationId}")
    public ResponseEntity<List<Entity>> getOrganizationEntities(@PathVariable(name = "organizationId") String organizationId) {
        return ResponseEntity.ok(this.entityService.getOrganizationEntities(organizationId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENTITIES_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable(name = "id") String id) {
        this.entityService.deleteEntity(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_ORGANIZATIONS_CREATE') or hasRole('ROLE_ORGANIZATIONS_UPDATE') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<Entity> saveEntity(@RequestBody EntityRequestDTO entityRequest) {
        return ResponseEntity.ok(this.entityService.saveEntity(entityRequest));
    }

}
