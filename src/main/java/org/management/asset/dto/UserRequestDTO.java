package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDTO implements Serializable {

    private static final Long serialVersionUID = -6040511937814466401L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("company")
    private String company;

    @JsonProperty("language")
    private String language;

    @JsonProperty("manager")
    private String manager;

    @JsonProperty("location")
    private String location;

    @JsonProperty("department")
    private String department;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("password")
    private String password;

    @JsonProperty("employeeNumber")
    private String employeeNumber;

    @JsonProperty("website")
    private String website;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    private String country;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("title")
    private String title;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("updateImage")
    private boolean updateImage;

    @JsonProperty("image")
    private MultipartFile image;

    @JsonProperty("roles")
    private String roles;

    @JsonProperty("groups")
    private String groups;

    @JsonProperty("updatePermissions")
    private boolean updatePermissions;

}
