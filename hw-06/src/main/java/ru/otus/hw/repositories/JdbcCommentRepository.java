package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JdbcCommentRepository implements CommentRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-graph");
        Map<String, Object> hints = new HashMap<>();
        hints.put(FETCH.getKey(), entityGraph);
        return Optional.ofNullable(em.find(Comment.class, id,hints));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book.id=:id", Comment.class)
            .setParameter("id", bookId);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        Comment comment = em.find(Comment.class, id);
        em.remove(comment);
    }
}
