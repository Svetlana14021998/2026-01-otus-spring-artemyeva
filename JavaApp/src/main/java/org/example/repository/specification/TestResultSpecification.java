package org.example.repository.specification;

import jakarta.persistence.criteria.Subquery;
import org.example.enums.CourseType;
import org.example.model.TestResult;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TestResultSpecification {

    public static Specification<TestResult> userIdIs(Long userId) {
        return (root, query, cb) ->
            cb.equal(root.get("userId"), userId);
    }

    public static Specification<TestResult> latestResults(Boolean isPassed) {
        return (root, query, cb) -> {
            Subquery<LocalDateTime> subquery = query.subquery(LocalDateTime.class);
            var subRoot = subquery.from(TestResult.class);

            var predicates = cb.and(
                cb.equal(subRoot.get("test"), root.get("test")),
                cb.equal(subRoot.get("userId"), root.get("userId"))
            );

            if (isPassed != null) {
                predicates = cb.and(predicates,
                    cb.equal(subRoot.get("isPassed"), isPassed)
                );
            }

            subquery.select(cb.greatest(subRoot.<LocalDateTime>get("passedDate")))
                .where(predicates);

            return cb.equal(root.<LocalDateTime>get("passedDate"), subquery);
        };
    }

    public static Specification<TestResult> passedIs(Boolean passed) {
        return (root, query, cb) ->
            cb.equal(root.get("isPassed"), passed);
    }

    public static Specification<TestResult> testIdIs(Long testId) {
        return (root, query, cb) ->
            cb.equal(root.get("test").get("id"), testId);
    }

    public static Specification<TestResult> courseTypeIs(CourseType type) {
        return (root, query, cb) ->
            cb.equal(root.get("test").get("course").get("courseType"), type);
    }
}
