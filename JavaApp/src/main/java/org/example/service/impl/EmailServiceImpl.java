package org.example.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.dto.EmailRequest;
import org.example.exception.SendMessagingException;
import org.example.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    private final AppConfig config;

    public void sendEmail(EmailRequest emailRequest) {
        try {
            log.info("Отправка email сообщения");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            //todo заглушка. письма должны отправляться на переданный email
            // helper.setTo(emailRequest.getTo());
            helper.setTo(config.getAdminEmail());
            helper.setSubject(emailRequest.getSubject());
            helper.setFrom(config.getAutoSendEmail());
            helper.setText(buildEmailContent(emailRequest), true);

            mailSender.send(message);
            log.info("email сообщение успешно отправлено");
        } catch (MessagingException e) {
            throw new SendMessagingException("Ошибка при отправке письма: " + e.getMessage());
        }
    }

    private String buildEmailContent(EmailRequest request) {
        Context context = new Context();
        context.setVariable("data", request.getData());

        return templateEngine.process(config.getMailPrintFormPath() + request.getTemplate().getTemplateName(), context);
    }
}
