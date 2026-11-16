package org.example.repository;

import org.example.enums.UserRole;
import org.example.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    List<AppUser> findByDeleteDateIs(LocalDate date);

    List<AppUser> findByRole(UserRole role);

    long countByRole(UserRole userRole);

    @Query("select u.id, " +
        "concat(u.lastName, ' ', u.firstName) as fullName, " +
        "count(c.id) as coursesCount " +
        "from AppUser u " +
        "LEFT JOIN Course c ON c.author.id = u.id " +
        "where u.role = 'TEACHER' " +
        "group by u.id, u.lastName, u.firstName " +
        "order by coursesCount desc")
    List<Object[]> getTeachersCourseCount();
}
