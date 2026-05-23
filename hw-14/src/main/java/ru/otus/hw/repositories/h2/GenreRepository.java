package ru.otus.hw.repositories.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.h2.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
