package org.example.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Getter
public class AppConfig {

    @Value("${app.phone}")
    private String phone;

    @Value("${app.email}")
    private String email;

    @Value("${app.telegram}")
    private String telegram;

    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${spring.mail.username}")
    private String autoSendEmail;

    @Value("${app.mail-printform-path}")
    private String mailPrintFormPath;

    @Value("http://localhost:${server.port}${server.servlet.context-path}")
    private String baseUrl;

    @Value("${app.users-photo-url}")
    private String baseUsersPhotoUrl;

    @Value("${server.servlet.context-path}")
    private String contextPath;
}
