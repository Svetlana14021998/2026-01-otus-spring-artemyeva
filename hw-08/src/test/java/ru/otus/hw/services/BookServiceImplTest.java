package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.converters.Author2AuthorDtoConverter;
import ru.otus.hw.converters.Book2BookDtoConverter;
import ru.otus.hw.converters.Genre2GenreDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Проверка работы BookServiceImpl")
public class BookServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Book2BookDtoConverter bookDtoConverter;

    @Autowired
    private Author2AuthorDtoConverter authorDtoConverter;

    @Autowired
    private Genre2GenreDtoConverter genreDtoConverter;

    @Test
    @DisplayName("Поиск книги по id")
    void findByIdTest() {
        // given
        // when
        Optional<BookDto> book = bookService.findById("1");

        // then
        assertThat(book).isPresent();

        BookDto bookDto = book.get();
        AuthorDto author = authorDtoConverter.convert(requireNonNull(mongoTemplate.findById("1", Author.class)));
        List<GenreDto> genres = List.of(
            genreDtoConverter.convert(mongoTemplate.findById("1", Genre.class)),
            genreDtoConverter.convert(mongoTemplate.findById("2", Genre.class)));

        assertAll(
            () -> assertThat(bookDto.getAuthor())
                .usingRecursiveComparison()
                .isEqualTo(author),
            () -> assertThat(bookDto.getGenres())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(genres));
    }

    @Test
    @DisplayName("Получение всех книг")
    void findAllTest() {
        // given
        // when
        List<BookDto> books = bookService.findAll();

        // then
        List<BookDto> bookDtos = mongoTemplate.findAll(Book.class).stream()
            .map(x -> bookDtoConverter.convert(x))
            .toList();

        assertThat(books)
            .usingRecursiveFieldByFieldElementComparator()
            .containsAll(bookDtos);
    }

    @Test
    @DisplayName("Сохранение книги")
    void saveTest() {
        // given
        String authorId = "1";
        String genreId1 = "1";
        String genreId2 = "2";

        // when
        BookDto book = bookService.insert("new Book", authorId, Set.of(genreId1, genreId2));

        // then
        AuthorDto author = authorDtoConverter.convert(requireNonNull(mongoTemplate.findById(authorId, Author.class)));
        List<GenreDto> genres = Set.of(requireNonNull(mongoTemplate.findById(genreId1, Genre.class)),
                requireNonNull(mongoTemplate.findById(genreId2, Genre.class))).stream()
            .map(x -> genreDtoConverter.convert(x))
            .toList();

        assertAll(
            () -> assertThat(book.getAuthor())
                .usingRecursiveComparison()
                .isEqualTo(author),
            () -> assertThat(book.getGenres())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(genres));
    }

    @Test
    @DisplayName("Изменение книги")
    void updateTest() {
        // given
        String authorId = "2";
        String genreId = "3";
        String newTitle = "update Book";

        // when
        BookDto book = bookService.update("1", newTitle, authorId, Set.of(genreId));

        // then
        AuthorDto author = authorDtoConverter.convert(requireNonNull(mongoTemplate.findById(authorId, Author.class)));
        List<GenreDto> genres = Set.of(requireNonNull(mongoTemplate.findById(genreId, Genre.class))).stream()
            .map(x -> genreDtoConverter.convert(x))
            .toList();

        assertAll(
            () -> assertThat(book.getTitle()).isEqualTo(newTitle),
            () -> assertThat(book.getAuthor())
                .usingRecursiveComparison()
                .isEqualTo(author),
            () -> assertThat(book.getGenres())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(genres));
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteTest() {
        //given
        String id = "1";

        //when
        bookService.deleteById(id);

        //then
        Book book = mongoTemplate.findById(id, Book.class);

        assertThat(book).isNull();
    }

    @Test
    @DisplayName("Удаление комментариев при удалении книги")
    void deleteCommentsWhenDeleteBookTest() {
        //given
        String id = "1";

        //when
        bookService.deleteById(id);

        //then
        Query query = new Query(Criteria.where("bookId").is(id));
        List<Comment> commentsForBook = mongoTemplate.find(query, Comment.class);

        assertThat(commentsForBook).isEmpty();
    }
}
