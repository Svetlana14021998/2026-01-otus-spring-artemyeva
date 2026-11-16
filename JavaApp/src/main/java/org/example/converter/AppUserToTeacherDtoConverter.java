package org.example.converter;

import org.example.model.AppUser;
import org.example.dto.TeacherDto;
import org.springframework.stereotype.Component;

@Component
public class AppUserToTeacherDtoConverter {

    public TeacherDto convert(AppUser user) {
        return TeacherDto.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .build();
    }
}
