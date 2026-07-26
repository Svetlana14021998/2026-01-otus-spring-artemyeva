package org.example.service;

import org.example.enums.EmailTemplate;
import org.example.view.UserQuestionView;

import java.util.Map;

public interface ContactService {

    void sendQuestionToAdmin(UserQuestionView userQuestionView, EmailTemplate template);

    void sendMsgToUser(String userEmail, Map<String, Object> data, EmailTemplate template);
}
