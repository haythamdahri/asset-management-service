package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.management.asset.listeners.CascadeSave;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 3848632254782687991L;

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String username;

    @JsonIgnore
    private String password;

    @Indexed(name = "email", unique = true)
    private String email;

    @DBRef
    @CascadeSave
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Organization organization;

    @DBRef
    @CascadeSave
    private Language language;

    @Indexed(name = "employeeNumber", unique = true)
    private String employeeNumber;
    private String title;

    @DBRef
    @CascadeSave
    private User manager;

    @DBRef
    @CascadeSave
    private Entity entity;

    @DBRef
    @CascadeSave
    private Location location;
    private String phone;
    private String website;
    private String jobTitle;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private boolean active;

    @JsonIgnore
    private String token;

    @JsonIgnore
    private LocalDateTime expiryDate;
    private LocalDateTime activationDate;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime deletionDate;
    private LocalDateTime lastLogin;
    private AssetFile avatar;
    private String notes;

    @DBRef
    @CascadeSave
    @EqualsAndHashCode.Exclude
    private Set<Group> groups;

    @DBRef
    @CascadeSave
    private Set<Role> roles;

    /**
     * Convenient method to add new role
     */
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        // Check that the role is not assigned to the user
        this.roles.add(role);
    }

    /**
     * Convenient method to check if a user has a role
     */
    public boolean hasRole(RoleType roleType) {
        Set<Role> allRoles = new HashSet<>();
        // Check roles
        try {
            if (this.roles != null) {
                allRoles = this.roles;
            }
        } catch (Exception ignored) {
        }
        try {
            if (this.groups != null) {
                allRoles.addAll(this.groups.stream().map(Group::getRoles).flatMap(Set::stream).collect(Collectors.toSet()));
            }
        } catch (Exception ignored) {
        }
        return allRoles.stream().anyMatch(role -> role.getRoleName().equals(roleType));
    }

    /**
     * Convenient method to add a group
     */
    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new HashSet<>();
        }
        this.groups.add(group);
    }

    /**
     * Check current user token validity
     *
     * @return boolean
     */
    public boolean checkTokenValidity() {
        return (this.expiryDate != null && this.token != null && !StringUtils.isEmpty(this.token) && this.expiryDate.isAfter(LocalDateTime.now()));
    }

}
