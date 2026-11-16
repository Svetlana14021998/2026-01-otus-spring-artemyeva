package org.example.repository.specification;

import org.example.enums.CourseType;
import org.example.model.Test;
import org.springframework.data.jpa.domain.Specification;

public class TestSpecification {

    public static Specification<Test> courseTypeIs(CourseType courseType) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("course").get("courseType"), courseType);
    }

    public static Specification<Test> authorIdIs(Long authorId) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("course").get("author").get("id"), authorId);
    }

    public static Specification<Test> titleIsEquals(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchTerm = "%" + title.trim().replaceAll("\\s+", " ") + "%";

            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")),
                searchTerm.toLowerCase());
        };
    }
}
