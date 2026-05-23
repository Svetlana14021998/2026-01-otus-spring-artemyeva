package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookDocument implements MongoDocument {
    @Id
    private String id;

    private String title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AuthorDocument author;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<GenreDocument> genres;
}
