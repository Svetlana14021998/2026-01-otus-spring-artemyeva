package org.example.repository;

import org.example.model.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {

    List<UserQuestion> findByIsSendFalseAndCreateAtAfter(LocalDateTime date);
}
