package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.config.AppConfig;
import org.example.model.AppUser;
import org.example.view.user.UserView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final AppConfig config;

    public UserView toView(AppUser user) {
        return UserView.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .birthday(user.getBirthday())
            .pathToPhoto(user.getPhotoName() != null ? config.getBaseUsersPhotoUrl() + user.getPhotoName() : null)
            .isActive(user.isActive())
            .phone(user.getPhone())
            .email(user.getEmail())
            .role(user.getRole().getRoleName())
            .build();
    }

    public AppUser toEntity(AppUser user, UserView view) {
        if (view.getFullName() != null) {
            String[] partName = view.getFullName().split(" ");
            user.setLastName(partName[0]);
            user.setFirstName(partName[1]);
        }
        user.setBirthday(view.getBirthday());
        user.setPhone(view.getPhone());
        user.setEmail(view.getEmail());
        return user;
    }
}
