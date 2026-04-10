package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService service;

    @GetMapping("/genres")
    public String getAllGenres(Model model) {
        List<GenreDto> genres = service.findAll();
        model.addAttribute("genres", genres);
        return "allGenres";
    }
}
