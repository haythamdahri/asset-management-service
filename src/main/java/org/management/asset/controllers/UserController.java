package org.management.asset.controllers;

import lombok.SneakyThrows;
import org.management.asset.bo.AssetFile;
import org.management.asset.bo.RoleType;
import org.management.asset.bo.User;
import org.management.asset.dto.*;
import org.management.asset.facades.IAuthenticationFacade;
import org.management.asset.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.userService.getUsers(search, this.authenticationFacade.getAuthentication().getName(), page, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS_CREATE_USERS') or hasRole('ROLE_USERS_EDIT_USERS') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/")
    public ResponseEntity<User> updateUser(@ModelAttribute UserRequestDTO userRequest) {
        // Save user
        return ResponseEntity.ok(this.userService.saveUser(userRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS_EDIT_USERS') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") String userId) {
        // Retrieve user
        User user = this.userService.getUser(userId);
        if( user == null ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.userService.getUser(userId));
    }

    @GetMapping(path = "/{id}/avatar/file")
    public ResponseEntity<byte[]> getUserAvatarFile(@PathVariable(name = "id") String userId) {
        // Retrieve AssetFile
        AssetFile assetFile = this.userService.getUserAvatar(userId);
        // Check file existence
        if (assetFile != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(assetFile.getMediaType()));
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(assetFile.getFile());
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS_DELETE_USERS') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") String userId) {
        // Delete User after validation
        this.userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Get authenticated user details
     *
     * @return
     */
    @GetMapping(path = "/profile")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName()));
    }

    /**
     * Get authenticated user details
     *
     * @return
     */
    @PutMapping(path = "/profile")
    public ResponseEntity<User> saveUserProfile(@RequestBody ProfileRequestDTO profileRequest) {
        return ResponseEntity.ok(this.userService.saveUser(profileRequest, this.authenticationFacade.getAuthentication().getName()));
    }

    /**
     * Check whether the user has a specific role or one of his groups has
     *
     * @param roleType: RoleType
     * @return
     */
    @GetMapping(path = "/roles/checking")
    public ResponseEntity<RolesCheckResponseDTO> checkRoleForCurrentUser(@RequestParam(name = "roleName") RoleType roleType) {
        User user = this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName());
            RolesCheckResponseDTO rolesCheckResponse = new RolesCheckResponseDTO();
            // Check if user has the role
            if (user.hasRole(roleType)) {
                rolesCheckResponse.setHasRole(true);
                rolesCheckResponse.setMessage("Role found");
            } else {
                rolesCheckResponse.setHasRole(false);
                rolesCheckResponse.setMessage("Role not found");
            }
            // Return response
            return ResponseEntity.ok(rolesCheckResponse);
    }

    /**
     * Check whether the user is an ADMIN
     *
     * @return
     */
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/roles/checking/admin")
    public ResponseEntity<RolesCheckResponseDTO> pingUserAdmin() {
        return ResponseEntity.ok().build();
    }

    /**
     * Check user privileges to add or edit a user
     *
     * @return
     */
    @GetMapping(path = "/roles/checking/users")
    public ResponseEntity<RolesCheckResponseDTO> checkUserEditRoleForCurrentUser() {
        User user = this.userService.getUserByEmail(this.authenticationFacade.getAuthentication().getName());
        RolesCheckResponseDTO rolesCheckResponse = new RolesCheckResponseDTO();
        List<RoleType> roleTypes = Stream.of(RoleType.ROLE_ADMIN, RoleType.ROLE_SUPER_USER,
                RoleType.ROLE_USERS_CREATE_USERS, RoleType.ROLE_USERS_EDIT_USERS).collect(Collectors.toList());
        for( RoleType roleType : roleTypes ) {
            // Check if user has the role
            if( user.hasRole(roleType) ) {
                rolesCheckResponse.setHasRole(true);
                rolesCheckResponse.setMessage("Role found");
                // Return response
                return ResponseEntity.ok(rolesCheckResponse);
            }
        }
        // Set role not found
        rolesCheckResponse.setHasRole(false);
        rolesCheckResponse.setMessage("Role not found");
        // Return response
        return ResponseEntity.ok(rolesCheckResponse);
    }

    /**
     * Retrieve users with only id, firstName, lastName, email
     *
     * @return List<UserDTO>
     */
    @GetMapping(path = "/custom")
    public ResponseEntity<List<UserDTO>> listCustomUsers() {
        return ResponseEntity.ok(this.userService.getCustomUsers());
    }

    /**
     * Password Reset Post Endpoint
     *
     * @param email: email
     * @return ResponseEntity<Void>
     */
    @GetMapping(path = "/passwordsreset")
    public ResponseEntity<Void> requestPasswordResetEmail(@RequestParam(name = "email") String email) {
        // Use service to request password reset
        this.userService.requestUserPasswordReset(email);
        return ResponseEntity.status(200).build();
    }

    @GetMapping(path = "/passwordsreset/tokensvalidity/{token}")
    public ResponseEntity<Void> checkTokenValidity(@PathVariable(name = "token") String token) {
        HttpStatus httpStatus;
        // Check token validity via user service
        if (this.userService.checkTokenValidity(token)) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return ResponseEntity.status(httpStatus).build();
    }

    /**
     * Password Reset Post Endpoint
     *
     * @param passwordResetRequest: passwordResetRequest
     * @return ResponseEntity<Void>
     */
    @PutMapping(path = "/passwordsreset")
    public ResponseEntity<Void> performResetPassword(@RequestBody PasswordResetRequestDTO passwordResetRequest) {
        // Use service to reset user password
        this.userService.resetUserPassword(passwordResetRequest.getToken(), passwordResetRequest.getPassword());
        return ResponseEntity.status(200).build();
    }

    /**
     * Update current authenticated user profile image
     * @param file
     * @return
     */
    @PutMapping(path = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateImage(@RequestParam(name = "image") MultipartFile file) {
        return ResponseEntity.ok(this.userService.updateUserImage(file, this.authenticationFacade.getAuthentication().getName()));
    }

}
