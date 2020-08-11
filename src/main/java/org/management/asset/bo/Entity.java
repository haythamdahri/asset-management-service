package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Haytham DAHRI
 * Une entité
 */
@Document(collection = "entities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity implements Serializable {

    private static final long serialVersionUID = 3021098015902959779L;

    @Id
    private String id;
    private String name;

    @DBRef
    private Organization organization;

    @DBRef
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<User> users;

    /**
     * Convenient method to add a user to an entity
     *
     * @param user: User
     */
    public void addUser(User user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(user);
        user.setEntity(this);
    }

}
