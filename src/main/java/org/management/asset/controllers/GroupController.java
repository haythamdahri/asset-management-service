package org.management.asset.controllers;

import org.management.asset.bo.Group;
import org.management.asset.dto.GroupDTO;
import org.management.asset.services.GroupService;
import org.management.asset.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<List<Group>> listGroups() {
        return ResponseEntity.ok(this.groupService.getGroups());
    }

    @GetMapping(path = "/counter")
    public ResponseEntity<Integer> getGroupsCounter() {
        return ResponseEntity.ok(this.groupService.getGroupsCounter());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<Group> saveGroup(@RequestBody GroupDTO group) {
        return ResponseEntity.ok(this.groupService.saveGroup(group));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/pages")
    public ResponseEntity<Page<Group>> getGroups(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.groupService.getGroups(name, page, size, direction, sort));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(name = "id") String groupId) {
        // Delete Group after validation
        this.groupService.deleteGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable(name = "id") String groupId) {
        return ResponseEntity.ok(this.groupService.getGroup(groupId));
    }

}
