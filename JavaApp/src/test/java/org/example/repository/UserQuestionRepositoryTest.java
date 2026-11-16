package org.example.repository;

import org.example.model.UserQuestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Тестирование QuestionRepository")
class UserQuestionRepositoryTest {

    @Autowired
    protected TestEntityManager em;

    @Autowired
    private UserQuestionRepository repository;

    @Test
    @DisplayName("Получение вопросов, по которым не было отправлено сообщение и дата создания которых меньше текущей даты - 1 день")
    void findByIsSendFalseAndCreateAtAfterTest() {
        //given
        UserQuestion question1 = UserQuestion.builder()
            .email("someMail1@mail.ru")
            .message("some Msg 1")
            .isSend(true)
            .name("someName1")
            .build();
        UserQuestion question2 = UserQuestion.builder()
            .email("someMail2@mail.ru")
            .message("some Msg 2")
            .isSend(false)
            .name("someName2")
            .build();
        UserQuestion question3 = UserQuestion.builder()
            .email("someMail3@mail.ru")
            .message("some Msg 3")
            .isSend(false)
            .name("someName3")
            .build();
        em.persistAndFlush(question1);
        em.persistAndFlush(question2);
        em.persistAndFlush(question3);

        Long id = (Long) em.getId(question2);
        UserQuestion question = em.find(UserQuestion.class, id);
        question.setCreateAt(LocalDateTime.now().minusDays(1).minusHours(1));
        em.merge(question);

        //when
        List<UserQuestion> questions = repository.findByIsSendFalseAndCreateAtAfter(LocalDateTime.now().minusDays(1));

        //then
        assertThat(questions).hasSize(1);
        assertThat(questions.get(0)).isEqualTo(question3);
    }
}