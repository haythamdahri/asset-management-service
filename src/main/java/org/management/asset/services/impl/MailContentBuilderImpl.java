package org.management.asset.services.impl;

import org.management.asset.services.MailContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author Haytam DAHRI
 */
@Service
public class MailContentBuilderImpl implements MailContentBuilder {

    private static final String TOKEN = "token";
    private static final String HOST = "host";
    private static final int SERVER_PORT = 3000;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${HOSTNAME}")
    private String hostname;

    @Override
    public String buildActivationEmail(String token) {
        Context context = new Context();
        context.setVariable(TOKEN, token);
        context.setVariable(HOST, this.hostname + ":" + SERVER_PORT);
        return templateEngine.process("mailing/activation-mail", context);
    }

    @Override
    public String buildPasswordResetEmail(String token) {
        Context context = new Context();
        context.setVariable(TOKEN, token);
        context.setVariable(HOST, this.hostname + ":" + SERVER_PORT);
        return templateEngine.process("mailing/password-reset", context);
    }

    @Override
    public String buildPasswordResetCompleteEmail() {
        Context context = new Context();
        context.setVariable(HOST, this.hostname + ":" + SERVER_PORT);
        return templateEngine.process("mailing/password-reset-complete", context);
    }

    @Override
    public String buildUpdateUserMailEmail(String token) {
        Context context = new Context();
        context.setVariable(TOKEN, token);
        context.setVariable(HOST, this.hostname + ":" + SERVER_PORT);
        return templateEngine.process("mailing/update-email-mail", context);
    }


}
