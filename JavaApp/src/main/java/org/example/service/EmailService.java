package org.example.service;

import org.example.dto.EmailRequestDto;

public interface EmailService {

    void sendEmail(EmailRequestDto emailRequestDto);
}
