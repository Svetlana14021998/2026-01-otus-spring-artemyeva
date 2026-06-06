package ru.otus.hw.migration.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MigrationTable {
    AUTHORS("authors_id_mapping","authors"),
    GENRES("genres_id_mapping","genres"),
    BOOKS("books_id_mapping","genres"),
    COMMENTS("comments_id_mapping","comments");

    private final String migrationTableName;

    private final String entityName;
}
