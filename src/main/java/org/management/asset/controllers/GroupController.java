package org.management.asset.controllers;

import org.management.asset.bo.Group;
import org.management.asset.services.GroupService;
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
@RequestMapping(path = "/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Group>> listGroups() {
        return ResponseEntity.ok(this.groupService.getGroups());
    }

}
