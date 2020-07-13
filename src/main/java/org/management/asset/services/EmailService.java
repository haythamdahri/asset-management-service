package org.management.asset.services;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @author Haytam DAHRI
 */
public interface EmailService {

    CompletableFuture<Boolean> sendSimpleMessage(String to, String subject, String text);

    CompletableFuture<Boolean> sendActivationEmail(String token, String to, String subject);

    CompletableFuture<Boolean> sendResetPasswordEmail(String token, String to, String subject);

    CompletableFuture<Boolean> sendResetPasswordCompleteEmail(String to, String subject);

    CompletableFuture<Boolean> sendUpdateUserMailEmail(String token, String to, String subject);

    CompletableFuture<Boolean> sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

}
