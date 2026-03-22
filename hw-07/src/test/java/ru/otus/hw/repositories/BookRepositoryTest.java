package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(scripts = "/db/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DisplayName("Проверка работы BookRepository")
class BookRepositoryTest extends AbstractRepositoryTest {

    private static final long FIRST_BOOK_ID = 1;

    private static final int BOOKS_COUNT = 3;

    @Autowired
    private BookRepository repository;

    @Test
    @DisplayName("Поиск книги по его id")
    void findBookByIdTest() {
        //given
        //when
        var book = repository.findById(FIRST_BOOK_ID);

        //then
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);

        assertThat(book).isPresent();
        Book actualBook = book.get();

        assertAll(
            () -> assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle()),
            () -> assertThat(actualBook.getAuthor()).usingRecursiveComparison().isEqualTo(expectedBook.getAuthor()),
            () -> assertThat(actualBook.getGenres()).usingRecursiveComparison().isEqualTo(expectedBook.getGenres()));
    }

    @Test
    @DisplayName("Поиск всех книг")
    void findAllBookTest() {
        //given
        //when
        var books = repository.findAll();

        //then
        assertThat(books).hasSize(BOOKS_COUNT);
        assertThat(books)
            .allMatch(b -> !b.getTitle().isEmpty())
            .allMatch(b -> b.getAuthor() != null)
            .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());
    }

    @Test
    @DisplayName("Проверка сохранения книги")
    void saveBookTest() {
        //given
        Author author = em.find(Author.class, 1);
        Genre genre1 = em.find(Genre.class, 1L);
        Genre genre2 = em.find(Genre.class, 2L);
        List<Genre> genres = new ArrayList<>(List.of(genre1, genre2));
        Book book = new Book(0, "New Book", author, genres);

        //when
        var savedBook = repository.save(book);
        em.flush();
        em.clear();

        //then
        assertThat(savedBook.getId()).isPositive();

        Book bookFromDB = em.find(Book.class, savedBook.getId());

        assertThat(bookFromDB).isNotNull();

        List<Long> savedGenreIds = savedBook.getGenres().stream().map(Genre::getId).toList();
        List<Long> fromBDGenreIds = bookFromDB.getGenres().stream().map(Genre::getId).toList();

        assertAll(
            () -> assertThat(savedBook.getTitle()).isEqualTo(bookFromDB.getTitle()),
            () -> assertThat(savedBook.getAuthor().getId()).isEqualTo(bookFromDB.getAuthor().getId()),
            () -> assertThat(savedGenreIds).usingRecursiveComparison().isEqualTo(fromBDGenreIds));
    }

    @Test
    @DisplayName("Проверка изменение книги")
    void updateBookTest() {
        //given
        var book = em.find(Book.class, FIRST_BOOK_ID);
        Author author = em.find(Author.class, 2);
        Genre genre1 = em.find(Genre.class, 5L);
        Genre genre2 = em.find(Genre.class, 6L);
        List<Genre> genres = new ArrayList<>(List.of(genre1, genre2));

        String newTitle = "new Title";
        book.setTitle(newTitle);
        book.setGenres(genres);
        book.setAuthor(author);

        //when
        repository.save(book);
        em.flush();
        em.clear();

        //then
        Book bookFromDB = em.find(Book.class, FIRST_BOOK_ID);

        List<Long> savedGenreIds = genres.stream().map(Genre::getId).toList();
        List<Long> fromBDGenreIds = bookFromDB.getGenres().stream().map(Genre::getId).toList();

        assertAll(
            () -> assertThat(book.getTitle()).isEqualTo(bookFromDB.getTitle()),
            () -> assertThat(book.getAuthor().getId()).isEqualTo(bookFromDB.getAuthor().getId()),
            () -> assertThat(savedGenreIds).usingRecursiveComparison().isEqualTo(fromBDGenreIds));
    }

    @Test
    @DisplayName("Проверка удаления книги")
    void deleteBookTest() {
        //when
        repository.deleteById(FIRST_BOOK_ID);

        //then
        Book bookFromDB = em.find(Book.class, FIRST_BOOK_ID);

        assertThat(bookFromDB).isNull();
    }
}