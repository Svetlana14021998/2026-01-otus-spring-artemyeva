package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        String query = """
            select b.id as book_id, title, author_id,full_name, g.id as genre_id, g.name
            from books b
            join authors a on a.id = b.author_id
            join books_genres bg on bg.book_id=b.id
            join genres g on g.id=bg.genre_id
            where b.id=:id
            """;
        MapSqlParameterSource parameter = new MapSqlParameterSource()
            .addValue("id", id);
        Book book = jdbc.query(query, parameter, new BookResultSetExtractor());
        if (book == null) {
            return Optional.empty();
        }
        return Optional.of(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var books = getAllBooksWithoutGenres();
        var relations = getAllGenreRelations();
        return mergeBooksInfo(books, genres, relations);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
            .addValue("id", id);
        jdbc.update("delete from books where id=:id", parameter);
        removeGenresRelationsFor(id);
    }

    private List<Book> getAllBooksWithoutGenres() {
        String query = """
            select b.id, b.title, a.id as author_id, a.full_name
            from books b
            join authors a
            on b.author_id = a.id""";
        return jdbc.query(query, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id,genre_id from books_genres",
            new BookGenreRelationMapper());
    }

    private List<Book> mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
        List<BookGenreRelation> relations) {
        Map<Long, Book> booksMap = booksWithoutGenres.stream()
            .collect(toMap(Book::getId, book -> book));
        Map<Long, Genre> genreMapMap = genres.stream()
            .collect(toMap(Genre::getId, genre -> genre));

        for (BookGenreRelation relation : relations) {
            var book = booksMap.get(relation.bookId());
            var genre = genreMapMap.get(relation.genreId());
            book.getGenres().add(genre);
        }
        return booksMap.values().stream().toList();
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("title", book.getTitle())
            .addValue("author_id", book.getAuthor().getId());
        jdbc.update("insert into books (title,author_id) values (:title,:author_id)",
            parameters, keyHolder);

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("title", book.getTitle())
            .addValue("author_id", book.getAuthor().getId())
            .addValue("id", book.getId());
        int updateCount = jdbc.update("update books set title=:title, author_id=:author_id where id=:id", parameters);
        if (updateCount == 0) {
            throw new EntityNotFoundException(String.format("Not found book with id=%s", book.getId()));
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        Set<Long> genreIds = book.getGenres().stream()
            .map(Genre::getId).collect(Collectors.toSet());
        Map[] parameters = genreIds.stream()
            .map(genreId -> Map.of(
                "bookId", book.getId(),
                "genreId", genreId
            ))
            .toArray(Map[]::new);

        jdbc.batchUpdate("insert into books_genres (book_id,genre_id) values (:bookId,:genreId)", parameters);
    }

    private void removeGenresRelationsFor(Book book) {
        removeGenresRelationsFor(book.getId());
    }

    private void removeGenresRelationsFor(long bookId) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
            .addValue("id", bookId);
        jdbc.update("delete from books_genres where book_id=:id", parameter);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");
            Author author = new Author(authorId, fullName);
            return new Book(id, title, author, new ArrayList<>());
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }
            long bookId = rs.getLong("book_id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");
            Book book = new Book(bookId, title, new Author(authorId, fullName), new ArrayList<>());

            do {
                long genreId = rs.getLong("genre_id");
                String name = rs.getString("name");
                Genre genre = new Genre(genreId, name);
                book.getGenres().add(genre);
            } while (rs.next());
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }
}
