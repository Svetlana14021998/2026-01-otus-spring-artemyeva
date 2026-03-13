package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;
import ru.otus.hw.validators.QuestionValidator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TestServiceImpl")
class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionValidator validator;

    private static Stream<Arguments> questions() {
        return Stream.of(
            Arguments.of(List.of(
                    new Question("Question1", List.of(
                        new Answer("Answer1", true),
                        new Answer("Answer2", false),
                        new Answer("Answer3", false)))),
                "One question"),
            Arguments.of(List.of(
                    new Question("Question1", List.of(
                        new Answer("Answer1", true),
                        new Answer("Answer2", false),
                        new Answer("Answer3", false))),
                    new Question("Question2", List.of(
                        new Answer("Answer1", false),
                        new Answer("Answer2", false),
                        new Answer("Answer3", false),
                        new Answer("Answer4", true),
                        new Answer("Answer5", false))),
                    new Question("Question3", List.of(
                        new Answer("Answer1", false),
                        new Answer("Answer2", false),
                        new Answer("Answer3", false),
                        new Answer("Answer4", true)))),
                "Some questions"),
            Arguments.of(Collections.EMPTY_LIST, "Not questions")
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Check Dependencies methods call")
    @MethodSource("questions")
    void checkDependenciesCallTest(List<Question> questions, String description) {
        //given
        when(validator.isQuestionsHasAnswers(any())).thenReturn(true);
        when(questionDao.findAll()).thenReturn(questions);
        lenient().when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
            .thenReturn(1);

        //when
        assertThatNoException().isThrownBy(() -> testService.executeTestFor(new Student("Ivan", "Ivanov")));

        //then
        //+1 because call this method for print empty str before other text
        int countOfCallPrintLine = questions.size() + 1;

        assertAll(
            () -> verify(questionDao, times(1)).findAll(),
            () -> verify(ioService, times(countOfCallPrintLine)).printLine(any()),
            () -> verify(ioService, times(1)).printFormattedLine(any()));
    }

    @Test
    @DisplayName("Check run method with empty answers for question")
    void checkTrowsExceptionWhenAnswersIsNullTest() {
        //given
        List<Question> questions = List.of(
            new Question("Question1", List.of(
                new Answer("Answer1", true),
                new Answer("Answer2", false),
                new Answer("Answer3", false))),
            new Question("Question2", List.of(
                new Answer("Answer1", false),
                new Answer("Answer2", false),
                new Answer("Answer3", false),
                new Answer("Answer4", true),
                new Answer("Answer5", false))),
            new Question("Question3", null));
        when(validator.isQuestionsHasAnswers(any())).thenReturn(false);
        when(questionDao.findAll()).thenReturn(questions);

        //when
        assertThatThrownBy(() -> testService.executeTestFor(new Student("Ivan", "Ivanov")))
            .isInstanceOf(QuestionIncorrectDataException.class)
            .hasMessage("Not answer for question");

        //then
        assertAll(
            () -> verify(questionDao, times(1)).findAll(),
            () -> verify(ioService, times(0)).printLine(any()),
            () -> verify(ioService, times(0)).printFormattedLine(any()));
    }
}