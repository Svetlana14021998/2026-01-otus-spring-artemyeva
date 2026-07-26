package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.UserQuestion;
import org.example.repository.UserQuestionRepository;
import org.example.service.UserQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQuestionServiceImpl implements UserQuestionService {

    private final UserQuestionRepository userQuestionRepository;

    @Transactional
    @Override
    public void save(UserQuestion question) {
        log.info("Сохранение вопроса от пользователя");
        UserQuestion userQuestion = userQuestionRepository.save(question);
        log.info("Вопрос от пользователя {} сохранен", userQuestion.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserQuestion> findByIsSendFalseAndCreateAtAfter(LocalDateTime localDateTime) {
        log.info("Получение всех неотправленных вопросов от пользователей, со временем отправки после {}", localDateTime);
        return userQuestionRepository.findByIsSendFalseAndCreateAtAfter(localDateTime);
    }
}
