package org.management.asset.controllers;

import org.management.asset.bo.RoleType;
import org.management.asset.bo.User;
import org.management.asset.dto.RolesCheckResponseDTO;
import org.management.asset.dto.UserDTO;
import org.management.asset.dto.UserRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.facades.IAuthenticationFacade;
import org.management.asset.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.userService.getUsers(search, this.authenticationFacade.getAuthentication().getName(), page, size));
    }

    @PutMapping(path = "/")
    public ResponseEntity<User> updateUser(@ModelAttribute UserRequestDTO userRequest) {
        User user = this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName());
        // Check Role And User
        if( !user.getId().equals(userRequest.getId())) {
            if (userRequest.getId() != null && (!user.hasRole(RoleType.ROLE_ADMIN) || !user.hasRole(RoleType.ROLE_USERS_CREATE_USERS))) {
                throw new BusinessException("Insufficient permissions");
            }
            if (userRequest.getId() != null && (!user.hasRole(RoleType.ROLE_ADMIN) || !user.hasRole(RoleType.ROLE_USERS_EDIT_USERS))) {
                throw new BusinessException("Insufficient permissions");
            }
        }
        // Save user
        return ResponseEntity.ok(this.userService.saveUser(userRequest));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long userId) {
        // Check role for the desired user
        User user = this.userService.getUser(userId);
        User currentUser = this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName());
        if (user != null) {
            // Check if user id is the path variable id
            if (!user.getEmail().equals(this.authenticationFacade.getAuthentication().getName()) &&
                    !currentUser.hasRole(RoleType.ROLE_ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(this.userService.getUser(userId));
    }

    /**
     * Check whether the user has a specific role or one of his groups has
     *
     * @param roleType: RoleType
     * @return
     */
    @GetMapping(path = "/roles/checking")
    @Transactional
    public ResponseEntity<RolesCheckResponseDTO> checkRoleForCurrentUser(@RequestParam(name = "roleName") RoleType roleType) {
        User user = this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName());
        if (user != null) {
            RolesCheckResponseDTO rolesCheckResponseDTO = new RolesCheckResponseDTO();
            rolesCheckResponseDTO.setSignOutRequired(false);
            // Check if user has the role
            if (user.hasRole(roleType)) {
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
     *
     * @return
     */
    @GetMapping(path = "/current")
    @Transactional
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName()));
    }

    /**
     * Retrieve users with only id, firstName, lastName, email
     * @return List<UserDTO>
     */
    @GetMapping(path = "/custom")
    public ResponseEntity<List<UserDTO>> listCustomUsers() {
        return ResponseEntity.ok(this.userService.getCustomUsers());
    }

}
