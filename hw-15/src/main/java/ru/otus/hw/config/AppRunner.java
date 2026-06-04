package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.SpecificationService;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final SpecificationService service;

    @Override
    public void run(ApplicationArguments args) {
        service.generateSpecifications();
    }
}
