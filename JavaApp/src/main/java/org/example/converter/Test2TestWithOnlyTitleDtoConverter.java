package org.example.converter;

import org.example.model.Test;
import org.example.dto.test.show.TestWithOnlyTitleDto;
import org.springframework.stereotype.Component;

@Component
public class Test2TestWithOnlyTitleDtoConverter {

    public TestWithOnlyTitleDto convert(Test test) {
        return TestWithOnlyTitleDto.builder()
            .id(test.getId())
            .title(test.getTitle())
            .build();
    }
}
