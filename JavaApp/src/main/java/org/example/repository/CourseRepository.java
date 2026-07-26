package org.example.repository;

import org.example.enums.CourseType;
import org.example.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @EntityGraph(value = "courseWithMaterialsAndAuthor")
    Page<Course> findAll(Specification<Course> spec, Pageable pageable);

    @EntityGraph(value = "courseWithMaterialsAndAuthor")
    Optional<Course> findById(Long id);

    List<Course> findByTestIdIsNullAndAuthorIdIs(Long id);

    @Query("select c.courseType, coalesce(round(avg(tr.percentage),2),null) as percentage from Course c " +
        "left join c.test t" +
        " left join TestResult tr ON tr.test.id = t.id" +
        " group by c.courseType" +
        " order by percentage desc nulls last")
    List<Object[]> getAveragePercentageByCourseType();

    List<Course> findByTestIdIsNull();

    @Query("select c.courseType from Course c group by c.courseType")
    List<CourseType> findDistinctByCourseType();

    @Query("SELECT u.id," +
        " concat(u.firstName, ' ', u.lastName) as full_name," +
        " coalesce(round(avg(tr.percentage), 2), null) as avg_percentage" +
        " from Course c" +
        " join c.author u" +
        " left join c.test t" +
        " left join TestResult tr ON tr.test.id = t.id" +
        " where u.role = 'TEACHER'" +
        " group by u.id, u.firstName, u.lastName" +
        " order by avg_percentage desc nulls last")
    List<Object[]> getAveragePercentageByTeacher();
}
