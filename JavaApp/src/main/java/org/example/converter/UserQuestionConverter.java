package org.example.converter;

import org.example.model.UserQuestion;
import org.example.dto.UserQuestionDto;
import org.springframework.stereotype.Component;

@Component
public class UserQuestionConverter {

    public UserQuestion toEntity(UserQuestionDto dto) {
        return UserQuestion.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .message(dto.getMessage())
            .id(dto.getId())
            .createAt(dto.getCreateAt())
            .build();
    }

    public UserQuestionDto toDto(UserQuestion question) {
        return UserQuestionDto.builder()
            .name(question.getName())
            .email(question.getEmail())
            .message(question.getMessage())
            .id(question.getId())
            .createAt(question.getCreateAt())
            .build();
    }
}
