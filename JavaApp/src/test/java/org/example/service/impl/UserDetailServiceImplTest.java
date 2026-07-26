package org.example.service.impl;

import org.example.exception.InactiveUserException;
import org.example.model.AppUser;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserDetailServiceImpl.class})
@DisplayName("Проверка UserDetailServiceImpl")
class UserDetailServiceImplTest {

    @Autowired
    private UserDetailServiceImpl service;

    @MockitoBean
    private UserRepository repository;

    private UserDetails appUser;

    @Test
    @DisplayName("Поиск пользователя по username, которого нет в бд")
    void findUserWithNotExistsUsernameTest() {
        //given
        when(repository.findByUsername(any()))
            .thenReturn(Optional.empty());

        //when
        String name = "name";
        assertThatThrownBy(
            () -> service.loadUserByUsername(name))
            //then
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage(String.format("Пользователь %s не найден", name));
    }

    @Test
    @DisplayName("Поиск пользователя, который деактивирован")
    void findUserIfUserInactiveTest() {
        //given
        when(repository.findByUsername(any()))
            .thenReturn(Optional.of(AppUser.builder()
                .isActive(false)
                .build()));

        //when
        String name = "name";
        assertThatThrownBy(
            () -> service.loadUserByUsername(name))
            //then
            .isInstanceOf(InactiveUserException.class)
            .hasMessage(String.format("Пользователь %s деактивирован", name));
    }

    @Test
    @DisplayName("Поиск пользователя, который деактивирован")
    void findUserIsUserInactiveTest() {
        //given
        String name = "name";
        AppUser user = AppUser.builder()
            .isActive(true)
            .username("name")
            .build();
        when(repository.findByUsername(any()))
            .thenReturn(Optional.of(user));

        //when
        assertThatNoException().isThrownBy(() ->
            appUser = service.loadUserByUsername(name));

        //then
        assertThat(appUser).isNotNull();
        assertThat((AppUser) appUser).isEqualTo(user);
    }
}