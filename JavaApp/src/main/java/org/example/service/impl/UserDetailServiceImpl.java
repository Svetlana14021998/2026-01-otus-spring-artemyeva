package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InactiveUserException;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Получение пользователя по username {}", username);
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь %s не найден", username)));
        if (!user.isActive()) {
            throw new InactiveUserException("Пользователь %s деактивирован".formatted(username));
        }
        return user;
    }
}
