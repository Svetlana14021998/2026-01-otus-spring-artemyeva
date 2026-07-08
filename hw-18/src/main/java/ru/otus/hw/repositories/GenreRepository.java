package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@Repository
@RepositoryRestResource(path = "genre")
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @RestResource(path = "ids", rel = "ids in")
    List<Genre> findAllByIdIn(Set<Long> ids);
}
