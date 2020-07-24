package org.management.asset.utils;

import org.apache.commons.io.FilenameUtils;
import org.management.asset.bo.Role;
import org.management.asset.bo.RoleType;
import org.management.asset.bo.User;
import org.management.asset.dto.RolesCheckResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Haytam DAHRI
 */
public class ApplicationUtils {

    private static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");


    private ApplicationUtils() {
    }

    /**
     * Retrieve file extension from a file name
     *
     * @param filename: File name
     * @return String
     */
    public static String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    /**
     * Mime message builder
     *
     * @param from:        Email Sender
     * @param to:          Email Receiver
     * @param subject:     Email Subject
     * @param text:        Email Text Content
     * @param message:     Email Message
     * @param isMultipart: Boolean to check if Multipart Exists
     * @return MimeMessageHelper
     * @throws MessagingException: Thrown on unhandled error
     */
    public static MimeMessageHelper buildMimeMessageHelper(String from, String to, String subject, String text, MimeMessage message, boolean isMultipart) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isMultipart);
        // return built helper
        return helper;
    }

    public static String escapeSpecialRegexChars(String str) {
        return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
    }

    public static RolesCheckResponseDTO checkUserHasRoles(List<RoleType> roleTypes, User user) {
        RolesCheckResponseDTO rolesCheckResponse = new RolesCheckResponseDTO();
        rolesCheckResponse.setHasRole(false);
        rolesCheckResponse.setMessage("Role not found");
        for( RoleType roleType : roleTypes ) {
            // Check if user has the role
            if( user.hasRole(roleType) ) {
                rolesCheckResponse.setHasRole(true);
                rolesCheckResponse.setMessage("Role found");
                break;
            }
        }
        return rolesCheckResponse;
    }

}
