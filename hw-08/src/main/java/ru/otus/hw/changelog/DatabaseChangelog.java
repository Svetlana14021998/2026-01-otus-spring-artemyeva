package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "dropDb", author = "sArt")
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "sArt")
    public void insertAuthors(AuthorRepository repository) {
        repository.saveAll(List.of(new Author("1", "Author_1"),
            new Author("2", "Author_2"),
            new Author("3", "Author_3"))
        );
    }

    @ChangeSet(order = "003", id = "insertGenre", author = "sArt")
    public void insertGenres(GenreRepository repository) {
        repository.saveAll(List.of(new Genre("1", "Genre_1"),
            new Genre("2", "Genre_2"),
            new Genre("3", "Genre_3"),
            new Genre("4", "Genre_4"),
            new Genre("5", "Genre_5"),
            new Genre("6", "Genre_6"))
        );
    }

    @ChangeSet(order = "004", id = "insertBook", author = "sArt")
    public void insertBooks(BookRepository repository, AuthorRepository authorRepository,
        GenreRepository genreRepository) {
        repository.saveAll(List.of(
            new Book("1", "Title_1", authorRepository.findById("1").get(),
                List.of(genreRepository.findById("1").get(),
                    genreRepository.findById("2").get())),
            new Book("2", "Title_2", authorRepository.findById("2").get(),
                List.of(genreRepository.findById("3").get(),
                    genreRepository.findById("4").get())),
            new Book("3", "Title_3", authorRepository.findById("3").get(),
                List.of(genreRepository.findById("5").get(),
                    genreRepository.findById("6").get()))
        ));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "sArt")
    public void insertComments(CommentRepository repository, BookRepository bookRepository) {
        repository.saveAll(List.of(
            new Comment("1", "first comment to book 1", bookRepository.findById("1").get()),
            new Comment("2", "second comment to book 1", bookRepository.findById("1").get()),
            new Comment("3", "first comment to book 2", bookRepository.findById("2").get())
        ));
    }
}
