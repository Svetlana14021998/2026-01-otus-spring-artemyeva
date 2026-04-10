package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Проверка контроллера для работы с книгами")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private GenreService genreService;
    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private MessageSource messageSource;

    private List<BookDto> books = List.of(
        new BookDto(1, "Title1", new AuthorDto(1, "Author1"), List.of(new GenreDto(1, "Genre1"), new GenreDto(2, "Genre2"))),
        new BookDto(2, "Title2", new AuthorDto(2, "Author2"), List.of(new GenreDto(3, "Genre3"), new GenreDto(4, "Genre4"))),
        new BookDto(3, "Title3", new AuthorDto(3, "Author3"), List.of(new GenreDto(5, "Genre5"), new GenreDto(6, "Genre6")))
    );

    private List<AuthorDto> authors = List.of(
        new AuthorDto(1, "Author1"),
        new AuthorDto(2, "Author2"),
        new AuthorDto(3, "Author3")
    );

    private List<GenreDto> genres = List.of(
        new GenreDto(1, "Genre1"),
        new GenreDto(2, "Genre2"),
        new GenreDto(3, "Genre3")
    );

    @Test
    @DisplayName("Получение всех книг")
    void getAllBooksTest() throws Exception {
        //given
        when(bookService.findAll()).thenReturn(books);

        //when
        mvc.perform(get("/books"))
            //then
            .andExpect(MockMvcResultMatchers.view().name("allBooks"))
            .andExpect(MockMvcResultMatchers.model().attribute("books", books));
    }

    @Test
    @DisplayName("Успешное изменение книги")
    void updateBookTest() throws Exception {
        //given
        long id = 1;
        String title = "Title";
        long authorId = 1;
        Long g1 = 1L;
        Long g2 = 2L;

        //when
        mvc.perform(post("/books/edit")
                .param("id", String.valueOf(id))
                .param("title", title)
                .param("author.id", String.valueOf(authorId))
                .param("genreIds", String.valueOf(g1), String.valueOf(g2)))
            //then
            .andExpect(view().name("redirect:/books"));
        verify(bookService, times(1)).update(id, title, authorId, Set.of(g1, g2));
    }

    @Test
    @DisplayName("Неуспешное изменение книги")
    void updateBookErrorTest() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        long id = 1;
        String title = "";
        long authorId = 1;
        Long g1 = 1L;
        Long g2 = 2L;

        //when
        mvc.perform(post("/books/edit")
                .param("id", String.valueOf(id))
                .param("title", title)
                .param("author.id", String.valueOf(authorId))
                .param("genreIds", String.valueOf(g1), String.valueOf(g2)))
            //then
            .andExpect(view().name("editPage"))
            .andExpect(model().attribute("authors", authors))
            .andExpect(model().attribute("genres", genres));

        verify(authorService, times(1)).findAll();
        verify(genreService, times(1)).findAll();
    }

    @Test
    @DisplayName("Успешное создание книги")
    void createBookTest() throws Exception {
        //given
        String title = "Title";
        long authorId = 1;
        Long g1 = 1L;
        Long g2 = 2L;

        //when
        mvc.perform(post("/books/create")
                .param("title", title)
                .param("authorId", String.valueOf(authorId))
                .param("genreIds", String.valueOf(g1), String.valueOf(g2)))
            //then
            .andExpect(view().name("redirect:/books"));
        verify(bookService, times(1)).insert(title, authorId, Set.of(g1, g2));
    }

    @Test
    @DisplayName("Неуспешное создание книги")
    void createBookErrorTest() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        String title = "";
        long authorId = 1;
        Long g1 = 1L;
        Long g2 = 2L;

        //when
        mvc.perform(post("/books/create")
                .param("title", title)
                .param("authorId", String.valueOf(authorId))
                .param("genreIds", String.valueOf(g1), String.valueOf(g2)))
            //then
            .andExpect(view().name("createPage"))
            .andExpect(model().attribute("authors", authors))
            .andExpect(model().attribute("genres", genres));
        verify(authorService, times(1)).findAll();
        verify(genreService, times(1)).findAll();
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteBookTest() throws Exception {
        //given
        //when
        mvc.perform(post("/books/delete").param("id", "1"))
            //then
            .andExpect(view().name("redirect:/books"));
        verify(bookService, times(1)).deleteById(anyLong());
    }
}