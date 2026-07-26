package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.Course2CourseShortViewConverter;
import org.example.converter.Course2CourseViewConverter;
import org.example.converter.Course2CourseViewMaterialsConverter;
import org.example.converter.CourseCreateView2CourseConverter;
import org.example.converter.CourseUpdateViewConverter;
import org.example.enums.CourseType;
import org.example.exception.EntityNotFoundException;
import org.example.exception.PermissionException;
import org.example.model.AppUser;
import org.example.model.Course;
import org.example.repository.CourseRepository;
import org.example.repository.specification.CourseSpecification;
import org.example.service.CourseService;
import org.example.view.course.CourseCreateView;
import org.example.view.course.CourseShortView;
import org.example.view.course.CourseUpdateView;
import org.example.view.course.CourseView;
import org.example.view.course.CourseWithMaterialsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    private final Course2CourseViewConverter course2CourseViewConverter;

    private final Course2CourseViewMaterialsConverter course2CourseViewMaterialsConverter;

    private final CourseCreateView2CourseConverter courseCreateView2CourseConverter;

    private final CourseUpdateViewConverter courseUpdateViewConverter;

    private final Course2CourseShortViewConverter course2CourseShortViewConverter;

    @Transactional(readOnly = true)
    @Override
    public Page<CourseView> findAll(Long authorId, String description, CourseType courseType, int pageNumber, int pageSize) {
        log.info("Поиск курсов");
        Specification<Course> spec = buildSpecification(authorId, description, courseType);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(spec, pageable).map(course2CourseViewConverter::convert);
    }

    @Transactional(readOnly = true)
    @Override
    public CourseView findById(Long id) {
        log.info("Поиск курса по id {}", id);
        return course2CourseViewConverter.convert(repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс не найден")));
    }

    @Transactional(readOnly = true)
    @Override
    public String getCourseDescription(Long id) {
        log.info("Получение описания курса по id {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс не найден"))
            .getDescription();
    }

    @Transactional(readOnly = true)
    @Override
    public Course findEntityById(Long id) {
        log.info("Поиск курса (Entity) по id {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));
    }

    @Transactional(readOnly = true)
    @Override
    public CourseShortView findByIdForTest(Long id) {
        log.info("Поиск краткой информации о курсе по id {}", id);
        return course2CourseShortViewConverter.convert(repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс не найден")));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseShortView> findCourseWithoutTestForTeacher(Long userId) {
        log.info("Поиск курсов, по которым нет тестов, для преподавателя id {}", userId);
        List<CourseShortView> courses = repository.findByTestIdIsNullAndAuthorIdIs(userId).stream()
            .map(course2CourseShortViewConverter::convert)
            .toList();
        log.info("Найдено {} курсов без тестов для преподавателя id {}", courses.size(), userId);
        return courses;
    }

    @Transactional(readOnly = true)
    @Override
    public CourseUpdateView findByIdToEdit(Long id) {
        log.info("Поиск курса id {} для модификации", id);
        return courseUpdateViewConverter.toView(repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс не найден")));
    }

    @Transactional(readOnly = true)
    @Override
    public CourseWithMaterialsView findByIdWithMaterials(Long id) {
        log.info("Поиск курса id {} и материалов по нему", id);
        return course2CourseViewMaterialsConverter.convert(repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс не найден")));
    }

    @Transactional(readOnly = true)
    @Override
    public long courseCount() {
        log.info("Получение количества всех курсов");
        long count = repository.count();
        log.info("Найдено {} курсов", count);
        return count;
    }

    @Transactional
    @Override
    public void deleteById(Long id, Long userId) {
        log.info("Удаление курса id {}", id);
        Course course = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Курс id=%d не найден".formatted(id)));
        if (!Objects.equals(course.getAuthor().getId(), userId)) {
            throw new PermissionException("Курс может быть удален только преподавателем, который его создал",
                "Вы не можете удалить курс, созданный другим преподавателем");
        }
        repository.delete(course);
        log.info("Курс id {} удален", id);
    }

    @Transactional
    @Override
    public void save(CourseCreateView courseCreateView, AppUser user) {
        log.info("Создание курса");
        Course course = courseCreateView2CourseConverter.convert(courseCreateView, user);
        course = repository.save(course);
        log.info("Курс id {} создан", course.getId());
    }

    @Transactional
    @Override
    public void update(Course course) {
        log.info("Изменение курса id {} при добавлении теста", course.getId());
        repository.save(course);
        log.info("Курс id {} изменен. Добавлен тест", course.getId());
    }

    @Transactional
    @Override
    public void update(CourseUpdateView courseUpdateView, Long userId) {
        Long courseId = courseUpdateView.getId();
        log.info("Изменение курса id {}", courseId);
        Course course = repository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException("Курс id=%d не найден".formatted(courseId)));
        if (!Objects.equals(course.getAuthor().getId(), userId)) {
            throw new PermissionException("Курс может быть изменен только преподавателем, который его создал",
                "Вы не можете изменить курс, созданный другим преподавателем");
        }
        course = courseUpdateViewConverter.toEntity(course, courseUpdateView);
        repository.save(course);
        log.info("Курс id {} изменен", course);
    }

    private static Specification<Course> buildSpecification(Long authorId, String description, CourseType courseType) {
        Specification<Course> spec = Specification.allOf();
        if (authorId != null) {
            spec = spec.and(CourseSpecification.authorIdIs(authorId));
        }
        if (description != null && !description.isBlank()) {
            spec = spec.and(CourseSpecification.descriptionIsEquals(description));
        }
        if (courseType != null) {
            spec = spec.and(CourseSpecification.courseTypeIs(courseType));
        }
        return spec;
    }
}
