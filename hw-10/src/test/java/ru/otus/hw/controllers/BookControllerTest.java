package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@DisplayName("Проверка работы BookController")
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

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
            new BookDto(1, "Title1",
                new AuthorDto(1, "Author1"),
                List.of(new GenreDto(1, "Genre1"), new GenreDto(2, "Genre2"))),
            new BookDto(2, "Title2",
                new AuthorDto(2, "Author2"),
                List.of(new GenreDto(1, "Genre3"), new GenreDto(2, "Genre4"))),
            new BookDto(3, "Title3",
                new AuthorDto(3, "Author3"),
                List.of(new GenreDto(5, "Genre5"), new GenreDto(6, "Genre6"))),
            new BookDto(4, "Title4",
                new AuthorDto(1, "Author1"),
                List.of(new GenreDto(1, "Genre1"), new GenreDto(6, "Genre6"))));

        when(bookService.findAll()).thenReturn(books);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/books"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("Получение книги по id")
    void findByIdTest() throws Exception {
        //given
        BookDto book = new BookDto(1, "Title1",
            new AuthorDto(1, "Author1"),
            List.of(new GenreDto(1, "Genre1"), new GenreDto(2, "Genre2")));

        when(bookService.findById(1)).thenReturn(Optional.of(book));

        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Создание книги")
    void createTest() throws Exception {
        //given
        BookDto book = new BookDto(0, "Title1",
            new AuthorDto(1, "Author1"),
            List.of(new GenreDto(1, "Genre1"), new GenreDto(2, "Genre2")));

        when(bookService.insert(any(BookDto.class))).thenReturn(book);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(book)))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Изменение книги")
    void updateTest() throws Exception {
        //given
        BookDto book = new BookDto(1, "Title1",
            new AuthorDto(1, "Author1"),
            List.of(new GenreDto(1, "Genre1"), new GenreDto(2, "Genre2")));

        when(bookService.update(any(BookDto.class))).thenReturn(book);

        //when
        mvc.perform(MockMvcRequestBuilders.put("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(book)))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteWithTest() throws Exception {
        //given
        doNothing().when(bookService).deleteById(anyLong());

        //when
        mvc.perform(MockMvcRequestBuilders.delete("/api/books/1"))
            //then
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удаление книги (с ошибкой)")
    void deleteWithErrorTest() throws Exception {
        //given
        doThrow(new IllegalArgumentException()).when(bookService).deleteById(anyLong());

        //when
        mvc.perform(MockMvcRequestBuilders.delete("/api/books/1"))
            //then
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получение комментариев по id книги")
    void findCommentsByBookIdTest() throws Exception {
        //given
        List<CommentDto> comments = List.of(
            new CommentDto(1,"Comment1",1,"BookTitle1"),
            new CommentDto(2,"Comment2",1,"BookTitle1"),
            new CommentDto(3,"Comment3",1,"BookTitle1")
        );

        when(commentService.findAllByBookId(1)).thenReturn(comments);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/books/1/comments"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(comments)));
    }
}