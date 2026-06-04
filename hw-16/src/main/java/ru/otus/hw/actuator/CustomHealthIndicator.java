package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final BookRepository repository;

    @Override
    public Health health() {
        return repository.findAll().isEmpty() ? Health.down()
            .status(Status.DOWN)
            .withDetail("message", "Not exists books!")
            .build() :
            Health.up().build();
    }
}
