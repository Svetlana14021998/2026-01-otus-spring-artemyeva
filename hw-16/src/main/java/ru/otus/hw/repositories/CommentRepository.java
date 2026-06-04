package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource(path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(long id);

    @RestResource(path = "book", rel = "book id")
    List<Comment> findAllByBookId(long id);
}
