package ru.otus.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) throws QuestionIncorrectDataException {
        if (value == null || value.isEmpty()) {
            throw new QuestionIncorrectDataException("Answers is null. Check csv file");
        }
        var valueArr = value.split("%");
        if (valueArr.length == 1) {
            throw new QuestionIncorrectDataException("Not found \"is correct\" for answer. Check csv file");
        }
        return new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));
    }
}
