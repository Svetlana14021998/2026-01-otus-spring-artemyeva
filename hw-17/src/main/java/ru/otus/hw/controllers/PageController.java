package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.UserDto;

@Controller
public class PageController {

    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registerPage";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

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
        model.addAttribute("bookId", id);
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
