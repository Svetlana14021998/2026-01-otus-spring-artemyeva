package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentDocument implements MongoDocument {
    @Id
    private String id;

    private String text;

    @DBRef
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BookDocument book;
}
