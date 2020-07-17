package org.management.asset.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public class Constants {

    public static final String STRING_SEPARATOR = ";";
    public static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif");
    public static final String ERROR = "An error occurred, please try again!";
    public static final String INVALID_USER_IMAGE = "Invalid user image!";
    public static final String INVALID_TOKEN = "Token does not belong to any user!";
    public static final String EXPIRED_TOKEN = "Token is expired!";
    public static final String EMAIL_NOT_FOUND = "Email does not belong to any user or email not enabled yet!";
    public static final String EMAIL_UPDATED = "Email has been updated successfully, please confirm your new email via received mail";
    public static final String EMAIL_ALREADY_USED = "Email Address is already used!";

    private Constants() {}

}
