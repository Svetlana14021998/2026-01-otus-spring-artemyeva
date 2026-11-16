package org.example.service;

import org.example.model.UserQuestion;

import java.time.LocalDateTime;
import java.util.List;

public interface UserQuestionService {

    void save(UserQuestion question);

    List<UserQuestion> findByIsSendFalseAndCreateAtAfter(LocalDateTime localDateTime);
}
