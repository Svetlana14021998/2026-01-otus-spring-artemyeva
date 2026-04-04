package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentsCascadeDeleteEventListener extends AbstractMongoEventListener<Book> {

    private final MongoOperations mongoOperations;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        String bookId = event.getSource().get("_id").toString();
        Query query = new Query(Criteria.where("bookId").is(bookId));
        mongoOperations.remove(query, Comment.class);
    }
}
