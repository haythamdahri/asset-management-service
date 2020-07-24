package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.management.asset.listeners.CascadeSave;
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
@Document(collection = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization implements Serializable {

    private static final long serialVersionUID = 6911109441298292437L;

    @Id
    private String id;

    @Indexed(name = "name", unique = true)
    private String name;
    private String description;
    private AssetFile image;

    @DBRef
    @CascadeSave
    private Set<Process> processes;

    @DBRef
    @CascadeSave
    @JsonIgnore
    private Set<User> users;

    /**
     * Convenient method to add a process
     */
    public void addProcess(Process process) {
        if (this.processes == null) {
            this.processes = new HashSet<>();
        }
        this.processes.add(process);
    }

    /**
     * Convenient method to add a user
     */
    public void addUser(User user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(user);
        user.setOrganization(this);
    }

}
