package org.example.repository.specification;

import org.example.enums.CourseType;
import org.example.model.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {

    public static Specification<Course> authorIdIs(Long id) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("author").get("id"), id);
    }

    public static Specification<Course> courseTypeIs(CourseType courseType) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("courseType"), courseType);
    }

    public static Specification<Course> descriptionIsEquals(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchTerm = "%" + description.trim().replaceAll("\\s+", " ") + "%";

            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")),
                searchTerm.toLowerCase());
        };
    }
}
