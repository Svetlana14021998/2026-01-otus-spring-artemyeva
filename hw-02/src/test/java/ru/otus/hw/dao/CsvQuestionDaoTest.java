package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test CsvQuestionDao")
class CsvQuestionDaoTest {

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private TestFileNameProvider testFileNameProvider;

    private List<Question> questions;

    @Test
    @DisplayName("Check throws QuestionIncorrectDataException when not answers for question")
    void checkThrowsQuestionIncorrectDataExceptionWhenNotAnswerForQuestionTest() {
        //given
        when(testFileNameProvider.getTestFileName()).thenReturn("test_questions_without_answer.csv");

        //when
        assertThatThrownBy(() -> csvQuestionDao.findAll())
            //then
            .isInstanceOf(RuntimeException.class)
            .hasRootCauseExactlyInstanceOf(QuestionIncorrectDataException.class)
            .hasRootCauseMessage("Answers is null. Check csv file");
    }

    @Test
    @DisplayName("Check throws QuestionIncorrectDataException when not is correct for answer")
    void checkThrowsQuestionIncorrectDataExceptionWhenNotIsCorrectForAnswerTest() {
        //given
        when(testFileNameProvider.getTestFileName()).thenReturn("test_questions_without_answer_is_correct.csv");

        //when
        assertThatThrownBy(() -> csvQuestionDao.findAll())
            //then
            .isInstanceOf(RuntimeException.class)
            .hasRootCauseExactlyInstanceOf(QuestionIncorrectDataException.class)
            .hasRootCauseMessage("Not found \"is correct\" for answer. Check csv file");
    }

    @Test
    @DisplayName("Check correct reading questions")
    void checkCorrectReadQuestionsTest() {
        //given
        when(testFileNameProvider.getTestFileName()).thenReturn("test_questions.csv");

        //when
        assertThatNoException().isThrownBy(() ->
            questions = csvQuestionDao.findAll());

        //then
        assertThat(questions).hasSize(3);
    }
}