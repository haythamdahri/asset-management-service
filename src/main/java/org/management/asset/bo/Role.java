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
 * @author Haytam DAHRI
 */
@Document(collection = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 6912880882655635525L;

    @Id
    private String id;

    private RoleType roleName;

    @DBRef
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> users;

    @DBRef
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Group> groups;

    /**
     * Convenient method to add a new group to the current role
     *
     * @param group: Group
     */
    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new HashSet<>();
        }
        this.groups.add(group);
    }

}
