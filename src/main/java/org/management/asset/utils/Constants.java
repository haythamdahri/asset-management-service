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
    public static final String PASSWORD_RESET_EMAIL_SENT = "Password reset email has been sent to your email successfully";
    public static final String ACCOUNT_NOT_ACTIVE = "Account not enabled yet!";
    public static final String ACCOUNT_ALREADY_ACTIVE = "Account already enabled";
    public static final String ACCOUNT_ACTIVATED_SUCCESSFULLY = "Account is enabled successfully";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password has been updated successfully";
    public static final String VALID_TOKEN = "Token is valid!";

    private Constants() {}

}
