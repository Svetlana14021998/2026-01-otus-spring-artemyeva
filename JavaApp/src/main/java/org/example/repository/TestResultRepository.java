package org.example.repository;

import org.example.model.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    @EntityGraph(value = "testResultWithTestAndCourse")
    Page<TestResult> findAll(Specification<TestResult> spec, Pageable pageable);

    void deleteByUserId(Long userId);

    @Query("select coalesce(round(avg(tr.percentage), 2), 0) from TestResult tr")
    Double getAveragePassPercentage();
}
