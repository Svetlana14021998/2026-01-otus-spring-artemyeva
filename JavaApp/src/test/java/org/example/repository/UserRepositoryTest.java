package org.example.repository;

import org.example.enums.CourseType;
import org.example.enums.UserRole;
import org.example.model.AppUser;
import org.example.model.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("Тестирование UserRepository")
@Sql(scripts = {"/db/create_users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class UserRepositoryTest {

    @Autowired
    protected TestEntityManager em;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Поиск пользователя по существующему username")
    void findByExistsUsernameTest() {
        //given
        //when
        Optional<AppUser> testUser1 = repository.findByUsername("testUser1");

        //then
        assertThat(testUser1).isPresent();
    }

    @Test
    @DisplayName("Поиск пользователя по несуществующему username")
    void findByNotExistsUsernameTest() {
        //given
        //when
        Optional<AppUser> testUser = repository.findByUsername("testUser123");

        //then
        assertThat(testUser).isEmpty();
    }

    @Test
    @DisplayName("Поиск пользователя по существующему email")
    void findByExistsEmailTest() {
        //given
        Optional<AppUser> testUser = repository.findByEmail("testUser1@javalearn.ru");

        //then
        assertThat(testUser).isPresent();
    }

    @Test
    @DisplayName("Поиск пользователя по несуществующему email")
    void findByNotExistsEmailTest() {
        //given
        Optional<AppUser> testUser = repository.findByEmail("testUser123@javalearn.ru");

        //then
        assertThat(testUser).isEmpty();
    }

    @Test
    @DisplayName("Поиск пользователей по дате удаления")
    void findByDeleteDateIsTest() {
        //given
        LocalDate deleteDate = LocalDate.now().plusDays(3);
        AppUser appUser = em.find(AppUser.class, 1L);
        appUser.setActive(false);
        appUser.setDeleteDate(deleteDate);
        em.merge(appUser);

        //when
        List<AppUser> testUser = repository.findByDeleteDateIs(deleteDate);

        //then
        assertThat(testUser).hasSize(1);
    }

    private static Stream<Arguments> usersRole() {
        return Stream.of(
            Arguments.of(UserRole.ADMIN, 1),
            Arguments.of(UserRole.TEACHER, 2),
            Arguments.of(UserRole.USER, 3)
        );
    }

    @ParameterizedTest
    @DisplayName("Поиск пользователей по роли")
    @MethodSource("usersRole")
    void findByRoleTest(UserRole role, int expectedSize) {
        //given
        //when
        List<AppUser> testUser = repository.findByRole(role);

        //then
        assertThat(testUser).hasSize(expectedSize);
    }

    @ParameterizedTest
    @DisplayName("Поиск количества пользователей по роли")
    @MethodSource("usersRole")
    void countByRoleTest(UserRole role, int expectedSize) {
        //given
        //when
        long count = repository.countByRole(role);

        //then
        assertThat(count).isEqualTo(expectedSize);
    }

    @Test
    @DisplayName("Получение количества курсов для каждого преподавателя")
    void teachersCourseCountTest() {
        //given
        AppUser appUser1 = em.find(AppUser.class, 1L);
        Course course1 = Course.builder()
            .author(appUser1)
            .courseType(CourseType.COLLECTIONS)
            .build();
        Course course2 = Course.builder()
            .author(appUser1)
            .courseType(CourseType.SQL)
            .build();
        AppUser appUser2 = em.find(AppUser.class, 3L);
        Course course3 = Course.builder()
            .author(appUser2)
            .courseType(CourseType.MULTITHREADING)
            .build();
        em.merge(course1);
        em.merge(course2);
        em.merge(course3);

        //when
        List<Object[]> teachersCourseCount = repository.getTeachersCourseCount();

        //then
        //преподаватель id=1
        Object[] res1 = teachersCourseCount.get(0);
        assertAll(
            () -> assertThat(res1[0]).isEqualTo(appUser1.getId()),
            () -> assertThat(res1[1]).isEqualTo(appUser1.getFullName()),
            () -> assertThat(res1[2]).isEqualTo(2L));

        //преподаватель id=3
        Object[] res2 = teachersCourseCount.get(1);
        assertAll(
            () -> assertThat(res2[0]).isEqualTo(appUser2.getId()),
            () -> assertThat(res2[1]).isEqualTo(appUser2.getFullName()),
            () -> assertThat(res2[2]).isEqualTo(1L));
    }
}