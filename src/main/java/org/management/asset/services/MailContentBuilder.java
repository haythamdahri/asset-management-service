package org.management.asset.services;

/**
 * @author Haytam DAHRI
 */
public interface MailContentBuilder {

    String buildActivationEmail(String token);

    String buildPasswordResetEmail(String token);

    String buildPasswordResetCompleteEmail();

    String buildUpdateUserMailEmail(String token);

}
