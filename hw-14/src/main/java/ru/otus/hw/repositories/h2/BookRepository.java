package ru.otus.hw.repositories.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.h2.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
