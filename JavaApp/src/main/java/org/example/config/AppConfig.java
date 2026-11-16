package org.example.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

    private String phone;

    private String email;

    private String telegram;

    private String adminEmail;

    @Value("${spring.mail.username}")
    private String autoSendEmail;

    private String mailPrintFormPath;

    private int port;

    private String baseUrl;

    private String usersPhotoUrl;

    private String contextPath;

    @PostConstruct
    public void init() {
        this.baseUrl = "http://localhost:" + port + contextPath;
    }
}
