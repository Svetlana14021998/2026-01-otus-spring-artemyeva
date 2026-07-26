package org.example.converter;

import org.example.model.AppUser;
import org.example.view.TeacherView;
import org.springframework.stereotype.Component;

@Component
public class AppUserToTeacherViewConverter {

    public TeacherView convert(AppUser user) {
        return TeacherView.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .build();
    }
}
