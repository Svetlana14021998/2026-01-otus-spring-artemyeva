package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(BookController.class)
@DisplayName("Проверка работы BookController")
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    @Test
    @DisplayName("Получение всех книг")
    void findAllTest() throws Exception {
        //given
        List<BookDto> books = List.of(
            new BookDto(1L, "Title1",
                new AuthorDto(1L, "Author1"),
                List.of(new GenreDto(1L, "Genre1"), new GenreDto(2L, "Genre2"))),
            new BookDto(2L, "Title2",
                new AuthorDto(2L, "Author2"),
                List.of(new GenreDto(1L, "Genre3"), new GenreDto(2L, "Genre4"))),
            new BookDto(3L, "Title3",
                new AuthorDto(3L, "Author3"),
                List.of(new GenreDto(5L, "Genre5"), new GenreDto(6L, "Genre6"))),
            new BookDto(4L, "Title4",
                new AuthorDto(1L, "Author1"),
                List.of(new GenreDto(1L, "Genre1"), new GenreDto(6L, "Genre6"))));

        when(bookService.findAll()).thenReturn(Flux.fromIterable(books));

        //when
        webTestClient.get()
            .uri("/api/books")
            .exchange()
            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(books));
    }

    @Test
    @DisplayName("Получение книги по id")
    void findByIdTest() throws Exception {
        //given
        BookDto book = new BookDto(1L, "Title1",
            new AuthorDto(1L, "Author1"),
            List.of(new GenreDto(1L, "Genre1"), new GenreDto(2L, "Genre2")));

        when(bookService.findById(1)).thenReturn(Mono.just(book));

        //when
        webTestClient.get()
            .uri("/api/books/1")
            .exchange()
            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(book));
    }

    @Test
    @DisplayName("Создание книги")
    void createTest() throws Exception {
        //given
        BookDto book = new BookDto(null, "Title1",
            new AuthorDto(1L, "Author1"),
            List.of(new GenreDto(1L, "Genre1"), new GenreDto(2L, "Genre2")));

        when(bookService.insert(any(BookDto.class))).thenReturn(Mono.just(book));

        //when
        webTestClient.post()
            .uri("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(mapper.writeValueAsString(book))
            .exchange()
            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(book));
    }

    @Test
    @DisplayName("Изменение книги")
    void updateTest() throws Exception {
        //given
        BookDto book = new BookDto(1L, "Title1",
            new AuthorDto(1L, "Author1"),
            List.of(new GenreDto(1L, "Genre1"), new GenreDto(2L, "Genre2")));

        when(bookService.update(any(BookDto.class))).thenReturn(Mono.just(book));

        //when
        webTestClient.put()
            .uri("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(mapper.writeValueAsString(book))
            .exchange()
            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(book));
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteWithTest() {
        //given
        when(bookService.deleteById(anyLong())).thenReturn(Mono.empty());

        //when
        webTestClient.delete()
            .uri("/api/books/1")
            .exchange()
            //then
            .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Получение комментариев по id книги")
    void findCommentsByBookIdTest() throws Exception {
        //given
        List<CommentDto> comments = List.of(
            new CommentDto(1L, "Comment1", 1L, "BookTitle1"),
            new CommentDto(2L, "Comment2", 1L, "BookTitle1"),
            new CommentDto(3L, "Comment3", 1L, "BookTitle1")
        );

        when(commentService.findAllByBookId(1)).thenReturn(Flux.fromIterable(comments));

        //when
        webTestClient.get()
            .uri("/api/books/1/comments")
            .exchange()
            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(comments));
    }
}