package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {

    private static final long serialVersionUID = -7696425062436068517L;

    @Id
    private String id;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zip;
    private AssetFile image;

}
