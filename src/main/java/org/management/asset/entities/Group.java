package org.management.asset.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "asset_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {

    private static final long serialVersionUID = -8419641167020237211L;

    @Id
    private String id;
    private String name;

    @DBRef
    @JsonIgnore
    private Set<Role> roles;

    @DBRef
    @JsonIgnore
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
}
