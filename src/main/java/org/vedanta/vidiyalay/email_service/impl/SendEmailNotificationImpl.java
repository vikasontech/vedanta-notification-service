/*
 *     Copyright (C) 2019  Vikas Kumar Verma
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.vedanta.vidiyalay.email_service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.vedanta.vidiyalay.email_service.EmailVM;
import org.vedanta.vidiyalay.email_service.config.ApplicationConfiguration;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
@Validated
public class SendEmailNotificationImpl implements org.vedanta.vidiyalay.email_service.SendEmailNotification {

    private final ApplicationConfiguration applicationConfiguration;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public SendEmailNotificationImpl(ApplicationConfiguration applicationConfiguration, JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.applicationConfiguration = applicationConfiguration;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

     @Override
     public void sendEmail(final EmailVM emailVM) {

        if (StringUtils.isEmpty(emailVM.getTo())
                && ObjectUtils.isEmpty(emailVM.getTos())) {
            log.debug("To address is null");
            return;
        }

        final MimeMessagePreparator messagePreparation = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom((StringUtils.isEmpty(emailVM.getFrom()) ?
                    applicationConfiguration.getMail().getFrom() : emailVM.getFrom()));
            messageHelper.setTo(Collections.singletonList(emailVM.getTo()).toArray(new String[0]));
            messageHelper.setSubject(emailVM.getSubject());
            messageHelper.setText(getMailBody(emailVM), true);
        };

        log.info("sending mail: {}", messagePreparation.toString());

        if (applicationConfiguration.getMail().isEnabled()) {
            javaMailSender.send(messagePreparation);
        } else {
            log.info("Email sending disabled in config see `application.mail.enabled`");
        }
        log.info("email sent!");
    }

    private String getMailBody(EmailVM emailVM) {
        if(ObjectUtils.isEmpty(emailVM.getTemplateFile())){
            return emailVM.getText();
        }
        return createMailTemplate(emailVM.getTemplateFile(), emailVM.getParams());
    }

    private String createMailTemplate(@NotNull final String templateFilename,
                                      @NotNull final Map<String, Object> params) {

        Context context = new Context();
        params.forEach(context::setVariable);
        return templateEngine.process(templateFilename, context);
    }

}
