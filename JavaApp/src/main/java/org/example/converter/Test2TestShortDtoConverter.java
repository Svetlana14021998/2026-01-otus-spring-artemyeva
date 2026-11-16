package org.example.converter;

import org.example.model.Test;
import org.example.dto.test.show.TestShortDto;
import org.springframework.stereotype.Component;

@Component
public class Test2TestShortDtoConverter {

    public TestShortDto convert(Test test) {
        return TestShortDto.builder()
            .id(test.getId())
            .title(test.getTitle())
            .needCorrectAnswerCount(test.getNeedCorrectAnswerCount())
            .questionsCount(test.getQuestions().size())
            .courseType(test.getCourse().getCourseType())
            .courseId(test.getCourse().getId())
            .author(test.getCourse().getAuthor().getFullName())
            .build();
    }
}
