package org.example.converter;

import org.example.model.UserQuestion;
import org.example.view.UserQuestionView;
import org.springframework.stereotype.Component;

@Component
public class UserQuestionConverter {

    public UserQuestion toEntity(UserQuestionView view) {
        return UserQuestion.builder()
            .name(view.getName())
            .email(view.getEmail())
            .message(view.getMessage())
            .id(view.getId())
            .createAt(view.getCreateAt())
            .build();
    }

    public UserQuestionView toView(UserQuestion question) {
        return UserQuestionView.builder()
            .name(question.getName())
            .email(question.getEmail())
            .message(question.getMessage())
            .id(question.getId())
            .createAt(question.getCreateAt())
            .build();
    }
}
