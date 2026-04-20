package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final BookService bookService;

    @GetMapping("/authors")
    public String getAuthorsPage() {
        return "allAuthors";
    }

    @GetMapping("/genres")
    public String getGenresPage() {
        return "allGenres";
    }

    @GetMapping("/books")
    public String getBooksPage() {
        return "allBooks";
    }

    @GetMapping("/books/{id}/comments")
    public String getBookWithAllInfo(@PathVariable Long id, Model model) {
        BookDto book = bookService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book with id=%d not found!".formatted(id)));
        model.addAttribute("bookId", id);
        model.addAttribute("bookTitle", book.getTitle());
        return "commentsForBook";
    }

    @GetMapping("/books/create")
    public String showCreateForm() {
        return "createPage";
    }

    @GetMapping("books/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        return "editPage";
    }
}
