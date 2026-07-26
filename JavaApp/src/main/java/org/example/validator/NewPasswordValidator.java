package org.example.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NewPasswordValidator {

    private final PasswordEncoder encoder;

    public Map<String, String> validate(String currentPassword, String newPassword, String newPasswordConfirm,
        String currentPasswordInDB) {
        Map<String, String> validationResult = new HashMap<>();

        boolean currentPasswordIsNull = currentPassword == null || currentPassword.isBlank();
        boolean newPasswordIsNull = newPassword == null || newPassword.isBlank();
        boolean newPasswordConfirmIsNull = newPasswordConfirm == null || newPasswordConfirm.isBlank();

        if (currentPasswordIsNull) {
            validationResult.put("currentPassword", "Необходимо заполнить текущий пароль");
        }

        if (newPasswordIsNull) {
            validationResult.put("newPassword", "Необходимо заполнить новый пароль");
        } else if (newPassword.length() < 5) {
            validationResult.put("newPassword", "Длина пароля должна быть не меньше 5 символов");
        }

        if (newPasswordConfirmIsNull) {
            validationResult.put("newPasswordConfirm", "Необходимо подтвердить новый пароль");
        }
        if (!newPasswordIsNull && !newPasswordConfirmIsNull) {
            if (!newPassword.equals(newPasswordConfirm)) {
                validationResult.put("newPasswordConfirm", "Некорретное подтверждение пароля");
            }
        }
        if (!currentPasswordIsNull && !newPasswordIsNull) {
            if (currentPassword.equals(newPassword)) {
                validationResult.put("newPassword", "Новый пароль не должен совпадать со старым");
            }
        }
        if (!currentPasswordIsNull) {
            if (!encoder.matches(currentPassword, currentPasswordInDB)) {
                validationResult.put("currentPassword", "Текущий пароль введен неправильно");
            }
        }
        return validationResult;
    }
}
