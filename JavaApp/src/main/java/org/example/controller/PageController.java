package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PageController {

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }
}
