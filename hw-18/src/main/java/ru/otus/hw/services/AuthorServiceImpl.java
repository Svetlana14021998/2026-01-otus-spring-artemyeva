package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.Author2AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final Author2AuthorDtoConverter converter;

    @Override
    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findAllFallback")
    @RateLimiter(name = "defaultRateLimiter")
    public List<AuthorDto> findAll() {
        log.info("find all authors");
        return authorRepository.findAll().stream()
            .map(converter::convert)
            .toList();
    }

    public List<AuthorDto> findAllFallback(Throwable ex) {
        return Collections.emptyList();
    }
}
