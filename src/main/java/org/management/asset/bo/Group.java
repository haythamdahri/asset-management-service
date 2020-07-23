package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {

    private static final long serialVersionUID = -8419641167020237211L;

    @Id
    private String id;

    @Indexed(name = "name", unique = true)
    private String name;
    private String description;

    @DBRef
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;

    @DBRef
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> users;

    /**
     * Convenient method to add a new user to the current group
     *
     * @param user: User
     */
    public void addUser(User user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(user);
        user.addGroup(this);
    }

    /**
     * Convenient method to add a new role to the current group
     *
     * @param role: Role
     */
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
        role.addGroup(this);
    }
}
