package org.example.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.AppUser;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NewUserValidator {

    private final UserRepository userRepository;

    public Map<String, String> validate(String username, String email) {
        Map<String, String> results = new HashMap<>();
        Optional<AppUser> findByUserName = userRepository.findByUsername(username);
        if (findByUserName.isPresent()) {
            results.put("username", "Пользователь с логином %s уже существует. Выберете другой логин"
                .formatted(username));
        }
        Optional<AppUser> findByEmail = userRepository.findByEmail(email);
        if (findByEmail.isPresent()) {
            results.put("email", "Пользователь с почтой %s уже существует. Укажите другую почту"
                .formatted(email));
        }
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regex)) {
            results.put("email", "Некорректный формат почты");
        }
        return results;
    }
}
