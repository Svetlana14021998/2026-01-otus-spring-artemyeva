package org.example.service;

import org.example.enums.EmailTemplate;
import org.example.dto.UserQuestionDto;

import java.util.Map;

public interface ContactService {

    void sendQuestionToAdmin(UserQuestionDto userQuestionDto, EmailTemplate template);

    void sendMsgToUser(String userEmail, Map<String, Object> data, EmailTemplate template);
}
