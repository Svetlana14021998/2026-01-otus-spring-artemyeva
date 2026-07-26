package org.example.repository;

import org.example.model.Test;
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
public interface TestRepository extends JpaRepository<Test, Long> {

    @EntityGraph(value = "testWithQuestionsCourseAndAuthor")
    Page<Test> findAll(Specification<Test> spec, Pageable pageable);

    @EntityGraph(value = "testWithAuthor")
    List<Test> findAll();

    @EntityGraph(value = "testWithQuestionsAndAnswers")
    Optional<Test> findById(Long id);

    @Query("select t.title,coalesce(round(avg(tr.percentage), 2), null) as percentage  from Test t " +
        "left join TestResult tr ON tr.test.id = t.id" +
        " group by t.title" +
        " order by percentage desc nulls last")
    List<Object[]> getAveragePercentageByTest();
}
