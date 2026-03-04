package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
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
import ru.otus.hw.exceptions.QuestionIncorrectDataException;

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
        when(questionDao.findAll()).thenReturn(questions);

        //when
        assertThatNoException().isThrownBy(() -> testService.executeTest());

        //then
        //+1 because call this method for print empty str before other text
        int countOfCallPrintLine = questions.size() + 1;

        assertAll(
            () -> verify(questionDao, times(1)).findAll(),
            () -> verify(ioService, times(countOfCallPrintLine)).printLine(any()),
            () -> verify(ioService, times(1)).printFormattedLine(any()));
    }

    private static Stream<Arguments> questionsWithIncorrectAnswers() {
        return Stream.of(
            Arguments.of(List.of(
                    new Question("Question1", null)),
                "Not answers for first question"),
            Arguments.of(List.of(
                    new Question("Question1", List.of(
                        new Answer("Answer1", true),
                        new Answer("Answer2", false),
                        new Answer("Answer3", false))),
                    new Question("Question2", null),
                    new Question("Question3", List.of(
                        new Answer("Answer1", false),
                        new Answer("Answer2", false),
                        new Answer("Answer3", false),
                        new Answer("Answer4", true)))),
                "Not answers for middle question"),
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
                    new Question("Question3", null)),
                "Not answers for last question")
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Check run method with empty answers for question")
    @MethodSource("questionsWithIncorrectAnswers")
    void checkTrowsExceptionWhenAnswersIsNullTest(List<Question> questions, String description) {
        //given
        int index = findIndexWithIncorrectAnswers(questions);
        when(questionDao.findAll()).thenReturn(questions);

        //when
        assertThatThrownBy(() -> testService.executeTest())
            .isInstanceOf(QuestionIncorrectDataException.class)
            .hasMessage("Not answers for question " + (index + 1));

        //then
        assertAll(
            () -> verify(questionDao, times(1)).findAll(),
            () -> verify(ioService, times(1)).printLine(any()),
            () -> verify(ioService, times(1)).printFormattedLine(any()));
    }

    private int findIndexWithIncorrectAnswers(List<Question> questions) {
        int index = -1;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).answers() == null) {
                index = i;
            }
        }
        return index == -1 ? questions.size() : index;
    }
}