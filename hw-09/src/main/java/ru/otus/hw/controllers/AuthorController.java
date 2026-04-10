package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        List<AuthorDto> authors = service.findAll();
        model.addAttribute("authors", authors);
        return "allAuthors";
    }
}
