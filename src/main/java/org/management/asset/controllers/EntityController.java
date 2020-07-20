package org.management.asset.controllers;

import org.management.asset.bo.Entity;
import org.management.asset.services.EntityService;
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
@RequestMapping(path = "/api/v1/entities")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Entity>> listEntities() {
        return ResponseEntity.ok(this.entityService.getEntities());
    }

}
