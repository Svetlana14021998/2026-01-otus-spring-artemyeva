package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.Genre2GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final Genre2GenreDtoConverter converter;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
            .map(converter::convert)
            .toList();
    }
}
