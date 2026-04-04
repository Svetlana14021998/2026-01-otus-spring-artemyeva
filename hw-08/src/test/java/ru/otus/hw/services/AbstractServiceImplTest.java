package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

@SpringBootTest
public class AbstractServiceImplTest {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("comments");

        Author a1 = new Author("1", "Author_1");
        Author a2 = new Author("2", "Author_2");

        Genre g1 = new Genre("1", "Genre_1");
        Genre g2 = new Genre("2", "Genre_2");
        Genre g3 = new Genre("3", "Genre_3");

        Book b1 = new Book("1", "Book_1", a1, List.of(g1, g2));
        Book b2 = new Book("2", "Book_2", a2, List.of(g2, g3));
        Book b3 = new Book("3", "Book_3", a2, List.of(g1, g3));

        Comment c1 = new Comment("1", "comment1", b1);
        Comment c2 = new Comment("2", "comment2", b1);
        Comment c3 = new Comment("3", "comment3", b2);

        mongoTemplate.insert(List.of(a1, a2), "authors");
        mongoTemplate.insert(List.of(g1, g2, g3), "genres");
        mongoTemplate.insert(List.of(b1, b2, b3), "books");
        mongoTemplate.insert(List.of(c1, c2, c3), "comments");
    }
}
