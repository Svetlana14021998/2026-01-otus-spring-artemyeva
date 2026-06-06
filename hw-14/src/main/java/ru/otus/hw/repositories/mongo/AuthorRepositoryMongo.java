package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.AuthorDocument;

public interface AuthorRepositoryMongo extends MongoRepository<AuthorDocument, String> {
}
