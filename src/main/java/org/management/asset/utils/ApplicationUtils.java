package org.management.asset.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

}
