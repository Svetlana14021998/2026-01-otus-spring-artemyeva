package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.config.AppConfig;
import org.example.model.AppUser;
import org.example.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final AppConfig config;

    public UserDto toDto(AppUser user) {
        return UserDto.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .birthday(user.getBirthday())
            .pathToPhoto(user.getPhotoName() != null ? config.getUsersPhotoUrl() + user.getPhotoName() : null)
            .isActive(user.isActive())
            .phone(user.getPhone())
            .email(user.getEmail())
            .role(user.getRole().getRoleName())
            .build();
    }

    public AppUser toEntity(AppUser user, UserDto dto) {
        if (dto.getFullName() != null) {
            String[] partName = dto.getFullName().split(" ");
            user.setLastName(partName[0]);
            user.setFirstName(partName[1]);
        }
        user.setBirthday(dto.getBirthday());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        return user;
    }
}
