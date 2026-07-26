package org.example.converter;

import org.example.model.Test;
import org.example.view.test.show.TestWithOnlyTitleView;
import org.springframework.stereotype.Component;

@Component
public class Test2TestWithOnlyTitleViewConverter {

    public TestWithOnlyTitleView convert(Test test) {
        return TestWithOnlyTitleView.builder()
            .id(test.getId())
            .title(test.getTitle())
            .build();
    }
}
