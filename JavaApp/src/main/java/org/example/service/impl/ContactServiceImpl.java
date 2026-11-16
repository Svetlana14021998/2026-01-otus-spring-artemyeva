package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.converter.UserQuestionConverter;
import org.example.dto.EmailRequestDto;
import org.example.enums.EmailTemplate;
import org.example.exception.SendMessagingException;
import org.example.model.UserQuestion;
import org.example.service.ContactService;
import org.example.service.EmailService;
import org.example.service.UserQuestionService;
import org.example.dto.UserQuestionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final EmailService emailService;

    private final AppConfig config;

    private final UserQuestionConverter converter;

    private final UserQuestionService userQuestionService;

    @Transactional
    @Override
    public void sendQuestionToAdmin(UserQuestionDto userQuestionDto, EmailTemplate template) {
        log.info("Отправка сообщения {} администратору", template.getDescription());
        UserQuestion question = converter.toEntity(userQuestionDto);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", userQuestionDto.getName());
            data.put("email", userQuestionDto.getEmail());
            data.put("message", userQuestionDto.getMessage());

            EmailRequestDto adminEmail = EmailRequestDto.builder()
                .to(config.getAdminEmail())
                .subject(template.getDescription())
                .template(template)
                .data(data)
                .build();
            emailService.sendEmail(adminEmail);
            question.setSend(true);
            userQuestionService.save(question);

            log.info("Сообщение {} успешно отправлено администратору", template.getDescription());
        } catch (SendMessagingException e) {
            log.error("Сообщение {} не было отправлено администратору", template.getDescription());
            question.setSend(false);
            userQuestionService.save(question);

            throw new SendMessagingException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void sendMsgToUser(String userEmail, Map<String, Object> data, EmailTemplate template) {
        log.info("Отправка сообщения {} пользователю {}", template.getDescription(), userEmail);

        try {
            EmailRequestDto adminEmail = EmailRequestDto.builder()
                .to(userEmail)
                .subject(template.getDescription())
                .template(template)
                .data(data)
                .build();
            emailService.sendEmail(adminEmail);

            log.info("Сообщение {} успешно отправлено пользователю {}", template.getDescription(), userEmail);
        } catch (SendMessagingException e) {
            log.error("Сообщение {} не было отправлено пользователю {}", template.getDescription(), userEmail);
        }
    }
}
