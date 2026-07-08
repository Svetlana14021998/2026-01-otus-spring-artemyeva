package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.Genre2GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final Genre2GenreDtoConverter converter;

    @Override
    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findAllFallback")
    @RateLimiter(name = "defaultRateLimiter")
    public List<GenreDto> findAll() {
        log.info("find All genres");
        return genreRepository.findAll().stream()
            .map(converter::convert)
            .toList();
    }

    public List<GenreDto> findAllFallback(Throwable ex) {
        log.error("Fallback for findAll triggered: {}", ex.getMessage());
        return Collections.emptyList();
    }
}
