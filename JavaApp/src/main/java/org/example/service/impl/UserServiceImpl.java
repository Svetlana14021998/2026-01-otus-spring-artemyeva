package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.converter.AppUserToTeacherDtoConverter;
import org.example.converter.UserConverter;
import org.example.dto.TeacherDto;
import org.example.dto.user.UserDto;
import org.example.dto.user.UserShortDto;
import org.example.enums.EmailTemplate;
import org.example.enums.UserRole;
import org.example.exception.ActiveUserException;
import org.example.exception.CantSaveFileException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.InactiveUserException;
import org.example.exception.ValidationException;
import org.example.exception.WrongPasswordException;
import org.example.helper.PasswordGenerator;
import org.example.model.AppUser;
import org.example.repository.UserRepository;
import org.example.service.ContactService;
import org.example.service.ImageService;
import org.example.service.TestResultService;
import org.example.service.UserService;
import org.example.validator.NewPasswordValidator;
import org.example.validator.NewUserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final ContactService contactService;

    private final AppConfig config;

    private final UserConverter converter;

    private final NewPasswordValidator passwordValidator;

    private final ImageService imageService;

    private final NewUserValidator userValidator;

    private final AppUserToTeacherDtoConverter teacherDtoConverter;

    private final TestResultService testResultService;

    private final PasswordGenerator passwordGenerator;

    @Transactional
    @Override
    public void createNewUser(UserShortDto userShortDto) {
        log.info("Создание нового пользователя");
        if (!userShortDto.getPassword().equals(userShortDto.getConfirmPassword())) {
            throw new WrongPasswordException("Некорректное подтверждение пароля");
        }
        Map<String, String> validationResult = userValidator.validate(userShortDto.getUsername(), userShortDto.getEmail());

        if (!validationResult.isEmpty()) {
            log.error("Возникла ошибка при создании пользователя: {}", String.join(" ,", validationResult.values()));
            throw new ValidationException("При создании нового пользователя возникла ошибка", validationResult);
        }
        String passwordForDB = encoder.encode(userShortDto.getPassword());
        AppUser user = AppUser.builder()
            .role(UserRole.USER)
            .username(userShortDto.getUsername())
            .password(passwordForDB)
            .email(userShortDto.getEmail())
            .isActive(true)
            .build();
        userRepository.save(user);
        Map<String, Object> data = new HashMap<>();
        data.put("username", userShortDto.getUsername());
        data.put("email", userShortDto.getEmail());
        data.put("supportEmail", config.getAdminEmail());
        data.put("baseUrl", config.getBaseUrl());
        contactService.sendMsgToUser(user.getEmail(), data, EmailTemplate.REGISTRATION_SUCCESS);
        log.info("Пользователь с username {} успешно создан", userShortDto.getUsername());
    }

    @Transactional
    @Override
    public void createManager(String username, String email) {
        log.info("Создание нового учителя с username {}", username);
        Map<String, String> validationResult = userValidator.validate(username, email);
        if (!validationResult.isEmpty()) {
            log.error("Возникла ошибка при создании учителя: {}", String.join(" ,", validationResult.values()));
            throw new ValidationException("При создании нового преподавателя возникла ошибка", validationResult);
        }
        String password = passwordGenerator.generate();
        AppUser user = AppUser.builder()
            .role(UserRole.TEACHER)
            .username(username)
            .password(password)
            .email(email)
            .isActive(true)
            .build();
        userRepository.save(user);
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("supportEmail", config.getAdminEmail());
        data.put("baseUrl", config.getBaseUrl());
        contactService.sendMsgToUser(user.getEmail(), data, EmailTemplate.REGISTRATION_TEACHER_SUCCESS);
        log.info("Новый учитель с username {} создан успешно", username);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByRole(UserRole role) {
        log.info("Поиск количества пользователей с ролью {}", role.getRoleName());
        long count = userRepository.countByRole(role);
        log.info("Найдено {} пользователей с ролью {}", count, role.getRoleName());
        return count;
    }

    @Transactional
    @Override
    public void resetPassword(String email) {
        log.info("Сброс пароля по адресу эл почты {}", email);
        Optional<AppUser> findByEmail = userRepository.findByEmail(email);
        if (findByEmail.isEmpty()) {
            throw new EntityNotFoundException("Пользователь с данной почтой не найден");
        }
        AppUser user = findByEmail.get();
        if (!user.isActive()) {
            throw new InactiveUserException("Учетная запись деактивирована");
        }
        String newPassword = passwordGenerator.generate();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("newPassword", newPassword);
        data.put("supportEmail", config.getAdminEmail());
        data.put("baseUrl", config.getBaseUrl());
        contactService.sendMsgToUser(user.getEmail(), data, EmailTemplate.RESET_PASSWORD);
        log.info("Пароль для пользователя {} успешно сброшен", user.getUsername());
    }

    @Transactional
    @Override
    public void recoverAccount(String email) {
        log.info("Восстановление аккаунта {}", email);
        Optional<AppUser> findByEmail = userRepository.findByEmail(email);
        if (findByEmail.isEmpty()) {
            throw new EntityNotFoundException("Пользователь с данной почтой не найден");
        }
        AppUser user = findByEmail.get();
        if (user.isActive()) {
            throw new ActiveUserException("Аккаунт активен. Восстановление не требуется");
        }
        user.setActive(true);
        user.setDeleteDate(null);
        userRepository.save(user);
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("supportEmail", config.getAdminEmail());
        data.put("baseUrl", config.getBaseUrl());
        contactService.sendMsgToUser(user.getEmail(), data, EmailTemplate.RECOVER_ACCOUNT);
        log.info("Аккаунт для пользователя {} успешно восстановлен", user.getUsername());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(Long id) {
        log.info("Поиск пользователя по id {}", id);
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден".formatted(id)));
        return converter.toDto(user);
    }

    @Transactional
    @Override
    public void inactive(Long id) {
        log.info("Деактивация пользователя id {}", id);
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден".formatted(id)));
        user.setActive(false);
        user.setDeleteDate(LocalDate.now().plusDays(30));
        userRepository.save(user);
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("supportEmail", config.getAdminEmail());
        data.put("baseUrl", config.getBaseUrl());
        contactService.sendMsgToUser(user.getEmail(), data, EmailTemplate.INACTIVE_ACCOUNT);
        log.info("Пользователь с  id {} деактивирован", id);
    }

    @Transactional
    @Override
    public void changePassword(AppUser user, String currentPassword, String newPassword, String confirmPassword) {
        log.info("Изменение пароля для пользователя {}", user.getUsername());
        Map<String, String> validationResults = passwordValidator.validate(currentPassword, newPassword, confirmPassword, user.getPassword());
        if (!validationResults.isEmpty()) {
            throw new ValidationException("При изменении пароля возникла ошибка", validationResults);
        }
        String encodeNewPassword = encoder.encode(newPassword);
        user.setPassword(encodeNewPassword);
        userRepository.save(user);
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("supportEmail", config.getAdminEmail());
        data.put("baseUrl", config.getBaseUrl());
        contactService.sendMsgToUser(user.getEmail(), data, EmailTemplate.CHANGE_PASSWORD);
        log.info("Пароль для пользователя {} успешно изменен", user.getUsername());
    }

    @Transactional
    @Override
    public void updateUserProfile(Long id, UserDto userDto, MultipartFile photoFile) {
        log.info("Реактирование пользователя id {}", id);
        AppUser appUser = userRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Пользователь с id=%d не найден".formatted(id)));
        appUser = converter.toEntity(appUser, userDto);
        if (photoFile != null && !photoFile.isEmpty()) {
            String photoName = appUser.getUsername() + ".jpg";
            try {
                imageService.savePhoto(photoFile, photoName, "users");
                appUser.setPhotoName(photoName);
                userRepository.save(appUser);
            } catch (CantSaveFileException e) {
                log.error("Не удалось сохранить фото");
                userRepository.save(appUser);
                throw new CantSaveFileException("Не удалось сохранить фото");
            }
        }
        log.info("Профиль пользователя id {} успешно изменен", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppUser> findUsersWithDeleteDateIsEquals(LocalDate date) {
        log.info("Поиск пользователей с датой удаления {}", date);
        return userRepository.findByDeleteDateIs(date);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя id {}", id);
        AppUser appUser = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден".formatted(id)));
        userRepository.delete(appUser);
        if (appUser.getPhotoName() != null && !appUser.getPhotoName().isBlank()) {
            imageService.deletePhoto(appUser.getPhotoName(), "users");
        }
        testResultService.deleteByUserId(appUser.getId());

        log.info("Пользователь id {} удален", id);
    }

    @Transactional
    @Override
    public void deleteTeacher(Long id) {
        log.info("Удаление преподавателя id {}", id);
        AppUser appUser = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден".formatted(id)));
        userRepository.delete(appUser);
        if (appUser.getPhotoName() != null && !appUser.getPhotoName().isBlank()) {
            imageService.deletePhoto(appUser.getPhotoName(), "users");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("username", appUser.getUsername());
        data.put("email", appUser.getEmail());
        data.put("supportEmail", config.getAdminEmail());
        contactService.sendMsgToUser(appUser.getEmail(), data, EmailTemplate.DELETE_TEACHER_ACCOUNT);
        log.info("Преподаватель id {} удален", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findByRole(UserRole userRole) {
        log.info("Поиск пользователей с ролью {}", userRole.getRoleName());
        List<UserDto> users = userRepository.findByRole(userRole).stream()
            .map(converter::toDto)
            .toList();
        log.info("Найдено {} пользователей с ролью {}", users.size(), userRole.getRoleName());
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeacherDto> findTeachers() {
        log.info("Поиск учителей");
        List<TeacherDto> teachers = userRepository.findByRole(UserRole.TEACHER).stream()
            .map(teacherDtoConverter::convert)
            .toList();
        log.info("Найдено {} преподавателей", teachers.size());
        return teachers;
    }
}
