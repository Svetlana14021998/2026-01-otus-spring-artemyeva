package org.example.service;

import org.example.enums.UserRole;
import org.example.model.AppUser;
import org.example.view.TeacherView;
import org.example.view.user.UserShortView;
import org.example.view.user.UserView;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    void createNewUser(UserShortView userShortView);

    void resetPassword(String email);

    void recoverAccount(String email);

    UserView findById(Long id);

    void inactive(Long id);

    void changePassword(AppUser user, String currentPassword, String newPassword, String confirmPassword);

    void updateUserProfile(Long id, UserView userView, MultipartFile photoFile);

    List<AppUser> findUsersWithDeleteDateIsEquals(LocalDate date);

    void deleteUser(Long id);

    void deleteTeacher(Long id);

    List<UserView> findByRole(UserRole userRole);

    List<TeacherView> findTeachers();

    void createManager(String username, String email);

    long countByRole(UserRole role);
}
