package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.CommentDocument;

public interface CommentRepositoryMongo extends MongoRepository<CommentDocument, String> {

}
