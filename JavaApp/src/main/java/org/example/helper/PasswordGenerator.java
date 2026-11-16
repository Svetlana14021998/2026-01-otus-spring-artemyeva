package org.example.helper;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String SPECIALS = "!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?";
    private static final String ALL = DIGITS + LETTERS + SPECIALS;
    private static final int PASSWORD_LENGTH = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(ALL.length());
            password.append(ALL.charAt(randomIndex));
        }

        return password.toString();
    }
}
