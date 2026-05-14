package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.AppUser;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser,Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
