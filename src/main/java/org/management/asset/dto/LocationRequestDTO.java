package org.management.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDTO implements Serializable {
    private static final long serialVersionUID = 9120701480102055101L;

    private String id;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zip;
    private MultipartFile image;
    private boolean updateImage;

}
