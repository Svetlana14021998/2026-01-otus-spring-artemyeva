package org.example.service;

import org.example.dto.EmailRequest;

public interface EmailService {

    void sendEmail(EmailRequest emailRequest);
}
