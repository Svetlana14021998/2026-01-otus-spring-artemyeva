package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.UserQuestionConverter;
import org.example.enums.EmailTemplate;
import org.example.exception.SendMessagingException;
import org.example.model.UserQuestion;
import org.example.service.ContactService;
import org.example.service.UserQuestionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetrySendQuestionMsgScheduler {

    private final UserQuestionService userQuestionService;

    private final ContactService contactService;

    private final UserQuestionConverter converter;

    @Scheduled(cron = "0 0 8-20 * * *")
    @Transactional
    public void retrySentFailedEmails() {
        log.info("run scheduler for send questions to admin");
        List<UserQuestion> questions = userQuestionService.findByIsSendFalseAndCreateAtAfter(LocalDateTime.now().minusDays(1));
        log.info("find {} questions for send", questions.size());
        for (UserQuestion q : questions) {
            try {
                contactService.sendQuestionToAdmin(converter.toDto(q), EmailTemplate.QUESTION);
                log.info("Success send email with id: {}", q.getId());
            } catch (SendMessagingException e) {
                log.error("Can't send email with id: {}", q.getId());
            }
        }
        log.info("finish scheduler for send questions to admin");
    }
}
