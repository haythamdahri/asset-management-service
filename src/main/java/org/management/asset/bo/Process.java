package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.management.asset.listeners.CascadeSave;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "processes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Process implements Serializable {

    private static final long serialVersionUID = -8923997747362630547L;

    @Id
    private String id;
    private String name;
    private String description;
    private boolean status;

    @DBRef
    private Process parentProcess;
    private ClassificationDICT classification;

    /**
     * Actifs
     */
    @DBRef
    @CascadeSave
    private Set<Asset> assets;

    @DBRef
    @CascadeSave
    private Organization organization;

    /**
     * Convenient method to add a new asset to the organization
     * @param asset
     */
    public void addAsset(Asset asset) {
        if( this.assets == null ) {
            this.assets = new HashSet<>();
        }
        this.assets.add(asset);
    }

}
