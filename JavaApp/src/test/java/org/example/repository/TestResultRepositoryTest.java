package org.example.repository;

import org.example.model.AppUser;
import org.example.model.Course;
import org.example.model.TestResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Тестирование TestResultRepository")
@Sql(scripts = {"/db/create_users.sql", "/db/create_tests.sql", "/db/create_courses.sql",
    "/db/create_test_results.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/db/create_test_results.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TestResultRepositoryTest {

    @Autowired
    protected TestEntityManager em;

    @Autowired
    private TestResultRepository repository;

    @Test
    @DisplayName("Средний % прохождения тестов")
    void averagePassPercentageTest() {
        //given
        Double expectedPercentage = 61.11;

        //when
        Double passPercentage = repository.getAveragePassPercentage();

        //then
        assertThat(passPercentage).isEqualTo(expectedPercentage);
    }

    @Test
    @DisplayName("Проверка удаления по id пользователя")
    void deleteByUserIdTest() {
        //given
        //when
        repository.deleteByUserId(2L);

        //then
        List<TestResult> results = em.getEntityManager()
            .createQuery("Select tr from TestResult tr", TestResult.class)
            .getResultList();

        assertThat(results).hasSize(4);
    }

    @Test
    @DisplayName("Проверка загрузки связанных сущностей")
    void validateNotProxyEntityTest() {
        //given
        //when
        Page<TestResult> results = repository
            .findAll(Specification.allOf(), Pageable.ofSize(5));

        //then
        Optional<TestResult> optional = results.getContent().stream().findAny();

        assertThat(optional.isPresent());

        TestResult result = optional.get();
        org.example.model.Test test = result.getTest();

        assertThat(test.getClass()).isEqualTo(org.example.model.Test.class);

        Course course = test.getCourse();

        assertThat(course.getClass()).isEqualTo(Course.class);

        AppUser author = course.getAuthor();

        assertThat(author.getClass()).isNotEqualTo(AppUser.class);
    }
}