package org.management.asset.controllers;

import org.management.asset.bo.RoleType;
import org.management.asset.bo.User;
import org.management.asset.dto.RolesCheckResponseDTO;
import org.management.asset.facades.IAuthenticationFacade;
import org.management.asset.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(this.userService.getUsers());
    }

    /**
     * Check whether the user has a specific role or one of his groups has
     * @param roleType: RoleType
     * @return
     */
    @GetMapping(path = "/roles/checking")
    @Transactional
    public ResponseEntity<RolesCheckResponseDTO> checkRoleForCurrentUser(@RequestParam(name = "roleName") RoleType roleType) {
        User user = this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName());
        if( user != null ) {
            RolesCheckResponseDTO rolesCheckResponseDTO = new RolesCheckResponseDTO();
            rolesCheckResponseDTO.setSignOutRequired(false);
            // Check if user has the role
            if( user.hasRole(roleType) ) {
                rolesCheckResponseDTO.setHasRole(true);
                rolesCheckResponseDTO.setMessage("Role found");
            } else {
                rolesCheckResponseDTO.setHasRole(false);
                rolesCheckResponseDTO.setMessage("Role not found");
            }
            // Return response
            return ResponseEntity.ok(rolesCheckResponseDTO);
        } else {
            // User must sign out and sign in again
            return ResponseEntity.ok(new RolesCheckResponseDTO("User must sign out and sign in again!", false, true));
        }
    }

    /**
     * Get authenticated user details
     * @return
     */
    @GetMapping(path = "/current")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName()));
    }

}
