package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 3848632254782687991L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "employeeNumber", nullable = false, unique = true)
    private String employeeNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(targetEntity = User.class, mappedBy = "manager")
    @JsonIgnore
    private List<User> subordinates;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "website", nullable = false)
    private String website;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zip", nullable = false)
    private String zip;

    @Column(name = "active", nullable = false)
    private boolean active;

    @JsonIgnore
    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "activationDate")
    private LocalDateTime activationDate;

    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "deletionDate")
    private LocalDateTime deletionDate;

    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = AssetFile.class)
    @JoinColumn(name = "avatar_id")
    private AssetFile avatar;

    @Column(name = "notes")
    private String notes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_groups", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private List<Group> groups;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_name")})
    @JsonIgnore
    private List<Role> roles;

    /**
     * Convenient method to add new role
     */
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    /**
     * Convenient method to check if a user has a role
     */
    public boolean hasRole(RoleType roleType) {
        List<Role> allRoles = new ArrayList<>();
        // Check roles
        if (this.roles != null) {
            allRoles = this.roles;
        }
        if (this.groups != null) {
            allRoles.addAll(this.groups.stream().map(Group::getRoles).flatMap(List::stream).collect(Collectors.toList()));
        }
        return allRoles.stream().anyMatch(role -> role.getRoleName().equals(roleType));
    }

    /**
     * Convenient method to add a group
     */
    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(group);
    }

    /**
     * Convenient method to add subordinates
     *
     * @param subordinate
     */
    public void addSubordinates(User subordinate) {
        if (this.subordinates == null) {
            this.subordinates = new ArrayList<>();
        }
        this.subordinates.add(subordinate);
    }

    public void setManager(User manager) {
        this.manager = manager;
        manager.addSubordinates(this);
    }

}
