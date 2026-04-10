package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    private final GenreService genreService;

    private final AuthorService authorService;

    private final MessageSource messageSource;

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "allBooks";
    }

    @GetMapping("/books/{id}")
    public String getBookWithAllInfo(@PathVariable Long id, Model model) {
        Optional<BookDto> book = bookService.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Entity with id %d not found".formatted(id));
        }
        model.addAttribute("book", book.get());
        List<CommentDto> comments = commentService.findAllByBookId(id);
        model.addAttribute("comments", comments);
        return "commentsForBook";
    }

    @GetMapping("books/edit")
    public String editBook(@RequestParam("id") Long id, Model model) {
        Optional<BookDto> book = bookService.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Entity with id %d not found".formatted(id));
        }
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();

        model.addAttribute("book", book.get());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editPage";
    }

    @PostMapping("books/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookDto bookDto,
        BindingResult bindingResult, @RequestParam(value = "genreIds", required = false) Set<Long> genreIds,
        Model model) {
        if (genreIds == null || genreIds.isEmpty()) {
            String text = messageSource.getMessage("genres-can-not-be-empty", null,
                LocaleContextHolder.getLocale());
            bindingResult.rejectValue("genres", "genre can`t be empty", text);
        }
        if (bindingResult.hasErrors()) {
            List<AuthorDto> authors = authorService.findAll();
            List<GenreDto> genres = genreService.findAll();

            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "editPage";
        }

        bookService.update(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor().getId(), genreIds);
        return "redirect:/books";
    }

    @GetMapping("/books/create")
    public String showCreateForm(Model model) {
        BookDto book = new BookDto();
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "createPage";
    }

    @PostMapping("/books/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto,
        BindingResult bindingResult, @RequestParam(value = "authorId", required = false) long authorId,
        @RequestParam(value = "genreIds", required = false) Set<Long> genreIds,
        Model model) {
        if (bindingResult.hasErrors()) {
            List<AuthorDto> authors = authorService.findAll();
            List<GenreDto> genres = genreService.findAll();

            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);

            return "createPage";
        }
        bookService.insert(bookDto.getTitle(), authorId, genreIds);
        return "redirect:/books";
    }

    @PostMapping(value = "/books/delete", params = "id")
    public String deleteBook(@RequestParam("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
