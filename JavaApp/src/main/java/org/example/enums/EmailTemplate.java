package org.example.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    QUESTION("question", "Вопрос с сайта от пользователя"),
    REGISTRATION_SUCCESS("registration-success", "Успешная регистрация на сайте JavaLearn"),
    RESET_PASSWORD("reset-password", "Сброс пароля на сайте JavaLearn"),
    RECOVER_ACCOUNT("recover-account-success", "Учетная запись успешно восстановлена"),
    INACTIVE_ACCOUNT("inactive-account", "Учетная запись деактивирована"),
    CHANGE_PASSWORD("change-password", "Изменение пароля на сайте JavaLearn"),
    DELETE_ACCOUNT_SOON("delete-account-soon", "Учетная запись будет удалена через 3 дня"),
    DELETE_ACCOUNT("delete-account", "Учетная запись удалена"),

    REGISTRATION_TEACHER_SUCCESS("registration-teacher-success", "Успешное создание профиля преподавателя на сайте JavaLearn"),
    DELETE_TEACHER_ACCOUNT("delete-teacher-account", "Учетная запись преподавателя удалена"),
    ;

    private final String templateName;

    private final String description;

    EmailTemplate(String templateName, String description) {
        this.templateName = templateName;
        this.description = description;
    }
}
