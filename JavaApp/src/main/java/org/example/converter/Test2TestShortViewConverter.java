package org.example.converter;

import org.example.model.Test;
import org.example.view.test.show.TestShortView;
import org.springframework.stereotype.Component;

@Component
public class Test2TestShortViewConverter {

    public TestShortView convert(Test test) {
        return TestShortView.builder()
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
