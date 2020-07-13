package org.management.asset.services.impl;

import org.management.asset.services.EmailService;
import org.management.asset.services.MailContentBuilder;
import org.management.asset.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Haytam DAHRI
 */
@Service
@Async
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.sender}")
    private String from;

    /**
     * Send simple email message implementation
     *
     * @param to:      Email receiver
     * @param subject: Email Subject
     * @param text:    Email Text
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            this.mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send professional email message with template implementation
     *
     * @param token:   Token to send with email as link
     * @param to:      Email receiver
     * @param subject: Email Subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendActivationEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildActivationEmail(token);
            MimeMessageHelper helper = ApplicationUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send professional email message with template implementation
     *
     * @param token:   Token to send with email as link
     * @param to:      Email receiver
     * @param subject: Email Subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendResetPasswordEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildPasswordResetEmail(token);
            MimeMessageHelper helper = ApplicationUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send professional email message with template implementation
     *
     * @param to:      Email receiver
     * @param subject: Email Subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendResetPasswordCompleteEmail(String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildPasswordResetCompleteEmail();
            MimeMessageHelper helper = ApplicationUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send user email update
     * Provide update token
     *
     * @param token:   Token to send with email as link
     * @param to:      Email receiver
     * @param subject: Email Subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendUpdateUserMailEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildUpdateUserMailEmail(token);
            MimeMessageHelper helper = ApplicationUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send email with attachment implementation
     *
     * @param to:               Email receiver
     * @param subject:          Email Subject
     * @param text:             Email Text
     * @param pathToAttachment: Attachments to send with Email: Files
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = ApplicationUtils.buildMimeMessageHelper(this.from, to, subject, text, message, true);
            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            // Add file attachment
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }


}
