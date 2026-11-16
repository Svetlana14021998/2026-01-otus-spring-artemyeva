package org.example.service;

import org.example.enums.UserRole;
import org.example.model.AppUser;
import org.example.dto.TeacherDto;
import org.example.dto.user.UserShortDto;
import org.example.dto.user.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    void createNewUser(UserShortDto userShortDto);

    void resetPassword(String email);

    void recoverAccount(String email);

    UserDto findById(Long id);

    void inactive(Long id);

    void changePassword(AppUser user, String currentPassword, String newPassword, String confirmPassword);

    void updateUserProfile(Long id, UserDto userDto, MultipartFile photoFile);

    List<AppUser> findUsersWithDeleteDateIsEquals(LocalDate date);

    void deleteUser(Long id);

    void deleteTeacher(Long id);

    List<UserDto> findByRole(UserRole userRole);

    List<TeacherDto> findTeachers();

    void createManager(String username, String email);

    long countByRole(UserRole role);
}
