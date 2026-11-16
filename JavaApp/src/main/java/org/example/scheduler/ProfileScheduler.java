package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.enums.EmailTemplate;
import org.example.exception.SendMessagingException;
import org.example.model.AppUser;
import org.example.service.ContactService;
import org.example.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileScheduler {

    private final UserService userService;

    private final ContactService contactService;

    private final AppConfig config;

    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    public void sendProfileWillBeInactive() {
        log.info("Запуск шедулера по отправке сообщений пользователям о предстоящем удалении профиля через 3 дня");
        LocalDate date = LocalDate.now().plusDays(3);
        List<AppUser> users = userService.findUsersWithDeleteDateIsEquals(date);
        log.info("Найдено {} пользователей для рассылки сообщений о предстоящем удалении", users.size());
        for (AppUser u : users) {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("username", u.getUsername());
                data.put("supportEmail", config.getAdminEmail());
                data.put("baseUrl", config.getBaseUrl());
                contactService.sendMsgToUser(u.getEmail(), data, EmailTemplate.DELETE_ACCOUNT_SOON);
                log.info("Успешно отправлено сообщение пользователю {} о предстоящем удалении аккаунта", u.getUsername());
            } catch (SendMessagingException e) {
                log.error("Не удалось отправить сообщение пользователю {} о предстоящем удалении аккаунта", u.getUsername());
            }
        }
        log.info("Завершение работы шедулера отправки сообщений пользователям о предстоящем удалении профиля");
    }

    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    public void deleteUsers() {
        log.info("Запуск шедулера по удалению профиля");
        LocalDate date = LocalDate.now();
        List<AppUser> users = userService.findUsersWithDeleteDateIsEquals(date);
        log.info("Найдено {} пользователей для удаления", users.size());
        for (AppUser u : users) {
            userService.deleteUser(u.getId());
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("username", u.getUsername());
                data.put("supportEmail", config.getAdminEmail());
                data.put("baseUrl", config.getBaseUrl());
                contactService.sendMsgToUser(u.getEmail(), data, EmailTemplate.DELETE_ACCOUNT);
                log.info("Успешно отправлено сообщение пользователю {} об удалении аккаунта", u.getUsername());
            } catch (SendMessagingException e) {
                log.error("Не удалось отправить сообщение пользователю {} об удалении аккаунта", u.getUsername());
            }
        }
        log.info("Завершение работы шедулера по удалению профиля");
    }
}
