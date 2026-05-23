package ru.otus.hw.config;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.converter.Author2AuthorDocumentConverter;
import ru.otus.hw.converter.Book2BookDocumentConverter;
import ru.otus.hw.converter.Comment2CommentDocumentConverter;
import ru.otus.hw.converter.Genre2GenreDocumentConverter;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.exception.IncorrectDataException;
import ru.otus.hw.exception.IncorrectMigrationDataException;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.h2.H2Entity;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.models.mongo.MongoDocument;
import ru.otus.hw.repositories.h2.AuthorRepository;
import ru.otus.hw.repositories.h2.BookRepository;
import ru.otus.hw.repositories.h2.CommentRepository;
import ru.otus.hw.repositories.h2.GenreRepository;
import ru.otus.hw.repositories.mongo.AuthorRepositoryMongo;
import ru.otus.hw.repositories.mongo.BookRepositoryMongo;
import ru.otus.hw.repositories.mongo.CommentRepositoryMongo;
import ru.otus.hw.repositories.mongo.GenreRepositoryMongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@Slf4j
public class JobConfig {

    private static final int CHUNK_SIZE = 5;

    private static final String AUTHORS_ID_TABLE = "authors_id_mapping";

    private static final String BOOKS_ID_TABLE = "books_id_mapping";

    private static final String GENRES_ID_TABLE = "genres_id_mapping";

    private static final String COMMENTS_ID_TABLE = "comments_id_mapping";

    private static final String CREATE_MIGRATION_TABLE_SQL = """
        create table if not exists %s (
            source_id BIGINT NOT NULL,
            target_id VARCHAR(255) NOT NULL,
            PRIMARY KEY (source_id))""";

    private static final String MERGE_SQL = """
        merge into %s (source_id, target_id)
        values (:sourceId, :targetId)""";

    private static final String FIND_EXISTS_TARGET_ID_SQL = """
        select target_id FROM %s WHERE source_id = :sourceId""";

    private static final String FIND_GENRES_TARGET_IDS = """
        select target_id from %s where source_id IN (:sourceIds)""";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private Author2AuthorDocumentConverter authorDocumentConverter;

    @Autowired
    private Genre2GenreDocumentConverter genreDocumentConverter;

    @Autowired
    private Book2BookDocumentConverter bookDocumentConverter;

    @Autowired
    private Comment2CommentDocumentConverter commentDocumentConverter;

    @PostConstruct
    public void initMappingTable() {
        jdbcTemplate.execute(CREATE_MIGRATION_TABLE_SQL.formatted(AUTHORS_ID_TABLE));
        jdbcTemplate.execute(CREATE_MIGRATION_TABLE_SQL.formatted(GENRES_ID_TABLE));
        jdbcTemplate.execute(CREATE_MIGRATION_TABLE_SQL.formatted(BOOKS_ID_TABLE));
        jdbcTemplate.execute(CREATE_MIGRATION_TABLE_SQL.formatted(COMMENTS_ID_TABLE));
    }

    @Bean
    public RepositoryItemReader<Author> authorReader(AuthorRepository repository) {
        RepositoryItemReader<Author> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemProcessor<Author, AuthorDocument> authorProcessor() {
        return author -> {
            String targetId = findTargetIdInMigrationTable(AUTHORS_ID_TABLE, author.getId());
            if (targetId == null) {
                return authorDocumentConverter.convert(author, null);
            }
            return authorDocumentConverter.convert(author, targetId);
        };
    }

    @Bean
    public Step migrateAuthorStep(RepositoryItemReader<Author> reader, ItemProcessor<Author, AuthorDocument> processor,
        AuthorRepositoryMongo authorRepositoryMongo) {
        List<Long> currentSourceIds = new ArrayList<>();
        List<String> currentTargetIds = new ArrayList<>();

        return new StepBuilder("migrateAuthorStep", jobRepository)
            .<Author, AuthorDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(authorRepositoryMongo::saveAll)
            .listener(createItemReadListener(currentSourceIds))
            .listener(createWriterListener(currentSourceIds, currentTargetIds, AUTHORS_ID_TABLE, "authors"))
            //для отладки
            .listener(createStepExecutionListener("authors"))
            .build();
    }

    @Bean
    public RepositoryItemReader<Genre> genreReader(GenreRepository repository) {
        RepositoryItemReader<Genre> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemProcessor<Genre, GenreDocument> genreProcessor() {
        return genre -> {
            String targetId = findTargetIdInMigrationTable(GENRES_ID_TABLE, genre.getId());
            if (targetId == null) {
                return genreDocumentConverter.convert(genre, null);
            }
            return genreDocumentConverter.convert(genre, targetId);
        };
    }

    @Bean
    public Step migrateGenreStep(RepositoryItemReader<Genre> reader, ItemProcessor<Genre, GenreDocument> processor,
        GenreRepositoryMongo genreRepositoryMongo) {
        List<Long> currentSourceIds = new ArrayList<>();
        List<String> currentTargetIds = new ArrayList<>();

        return new StepBuilder("migrateGenreStep", jobRepository)
            .<Genre, GenreDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(genreRepositoryMongo::saveAll)
            .listener(createItemReadListener(currentSourceIds))
            .listener(createWriterListener(currentSourceIds, currentTargetIds, GENRES_ID_TABLE, "genres"))
            //для отладки
            .listener(createStepExecutionListener("genres"))
            .build();
    }

    @Bean
    public RepositoryItemReader<Book> bookReader(BookRepository repository) {
        RepositoryItemReader<Book> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemProcessor<Book, BookDocument> bookProcessor(AuthorRepositoryMongo authorRepositoryMongo,
        GenreRepositoryMongo genreRepositoryMongo) {
        return book -> {
            AuthorDocument authorDocument = findAuthorDocumentForBook(book, authorRepositoryMongo);
            List<GenreDocument> genreDocuments = findGenreDocumentsForBook(book, genreRepositoryMongo);

            String targetId = findTargetIdInMigrationTable(BOOKS_ID_TABLE, book.getId());
            if (targetId == null) {
                return bookDocumentConverter.convert(book, null, authorDocument, genreDocuments);
            }
            return bookDocumentConverter.convert(book, targetId, authorDocument, genreDocuments);
        };
    }

    @Bean
    public Step migrateBookStep(RepositoryItemReader<Book> reader, ItemProcessor<Book, BookDocument> processor,
        BookRepositoryMongo bookRepositoryMongo) {
        List<Long> currentSourceIds = new ArrayList<>();
        List<String> currentTargetIds = new ArrayList<>();

        return new StepBuilder("migrateBookStep", jobRepository)
            .<Book, BookDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(bookRepositoryMongo::saveAll)
            .listener(createItemReadListener(currentSourceIds))
            .listener(createWriterListener(currentSourceIds, currentTargetIds, BOOKS_ID_TABLE, "books"))
            //для отладки
            .listener(createStepExecutionListener("books"))
            .build();
    }

    @Bean
    public RepositoryItemReader<Comment> commentReader(CommentRepository repository) {
        RepositoryItemReader<Comment> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemProcessor<Comment, CommentDocument> commentProcessor(BookRepositoryMongo bookRepositoryMongo) {
        return comment -> {
            BookDocument bookDocument = findBookDocForComment(comment, bookRepositoryMongo);
            String targetId = findTargetIdInMigrationTable(COMMENTS_ID_TABLE, comment.getId());
            if (targetId == null) {
                return commentDocumentConverter.convert(comment, null, bookDocument);
            }
            return commentDocumentConverter.convert(comment, targetId, bookDocument);
        };
    }

    @Bean
    public Step migrateCommentStep(RepositoryItemReader<Comment> reader, ItemProcessor<Comment,
            CommentDocument> processor,
        CommentRepositoryMongo commentRepositoryMongo) {
        List<Long> currentSourceIds = new ArrayList<>();
        List<String> currentTargetIds = new ArrayList<>();

        return new StepBuilder("migrateCommentStep", jobRepository)
            .<Comment, CommentDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(commentRepositoryMongo::saveAll)
            .listener(createItemReadListener(currentSourceIds))
            .listener(createWriterListener(currentSourceIds, currentTargetIds, COMMENTS_ID_TABLE, "comments"))
            //для отладки
            .listener(createStepExecutionListener("comments"))
            .build();
    }

    @Bean
    public Job migrateJob(Step migrateAuthorStep, Step migrateGenreStep, Step migrateBookStep, Step migrateCommentStep) {
        Flow authorFlow = new FlowBuilder<Flow>("authorFlow")
            .start(migrateAuthorStep)
            .build();

        Flow genreFlow = new FlowBuilder<Flow>("genreFlow")
            .start(migrateGenreStep)
            .build();

        Flow parallelAuthorsAndGenres = new FlowBuilder<Flow>("parallelAuthorsAndGenres")
            .start(authorFlow)
            .split(taskExecutor())
            .add(genreFlow)
            .build();

        return new JobBuilder("migrateJob", jobRepository)
            .start(parallelAuthorsAndGenres)
            .next(migrateBookStep)
            .next(migrateCommentStep)
            .end()
            .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setThreadNamePrefix("parallel-");
        executor.initialize();
        return executor;
    }

    private void saveIdsIntoMigratedTable(List<Long> sourceIds, List<String> targetIds,
        String tableName, String entityName) {
        if (sourceIds.isEmpty() || targetIds.isEmpty()) {
            log.warn("Empty lists for table {}, sourceIds size: {}, targetIds size: {}, skipping save",
                tableName, sourceIds.size(), targetIds.size());
            sourceIds.clear();
            targetIds.clear();
            return;
        }

        if (sourceIds.size() != targetIds.size()) {
            throw new RuntimeException("ID mapping size mismatch for %s ids: %s. Data not saved"
                .formatted(entityName, sourceIds.stream().map(Objects::toString).collect(
                    Collectors.joining(","))));
        }
        save(sourceIds, targetIds, tableName);
    }

    private void save(List<Long> sourceIds, List<String> targetIds, String tableName) {
        Map[] batchValues = IntStream.range(0, sourceIds.size())
            .mapToObj(i -> Map.of(
                "sourceId", sourceIds.get(i),
                "targetId", targetIds.get(i)
            ))
            .toArray(Map[]::new);

        namedJdbcTemplate.batchUpdate(MERGE_SQL.formatted(tableName), batchValues);
        sourceIds.clear();
        targetIds.clear();
    }

    private String findTargetIdInMigrationTable(String tableName, Long sourceId) {
        List<String> results = namedJdbcTemplate.queryForList(
            FIND_EXISTS_TARGET_ID_SQL.formatted(tableName),
            Map.of("sourceId", sourceId),
            String.class);
        return results.isEmpty() ? null : results.get(0);
    }

    private AuthorDocument findAuthorDocumentForBook(Book book, AuthorRepositoryMongo authorRepositoryMongo) {
        if (book.getAuthor() == null) {
            throw new IncorrectDataException("Author for book with sourceId=%d is null".formatted(book.getId()));
        }

        long authorSourceId = book.getAuthor().getId();
        String authorTargetId = findTargetIdInMigrationTable(AUTHORS_ID_TABLE, authorSourceId);

        if (authorTargetId == null) {
            throw new IncorrectMigrationDataException("Not found targetId to author with sourceId=%d"
                .formatted(authorSourceId));
        }
        return authorRepositoryMongo.findById(authorTargetId)
            .orElseThrow(() -> new EntityNotFoundException("Author with targetId=%s not found"
                .formatted(authorTargetId)));
    }

    private List<GenreDocument> findGenreDocumentsForBook(Book book, GenreRepositoryMongo genreRepositoryMongo) {
        List<Long> genreSourceIds = getGenresIdsFromBook(book);
        String findGenresTargetIds = FIND_GENRES_TARGET_IDS.formatted(GENRES_ID_TABLE);
        Map<String, Object> params = Collections.singletonMap("sourceIds", genreSourceIds);

        List<String> genreTargetIds = namedJdbcTemplate.queryForList(findGenresTargetIds, params, String.class);
        if (genreTargetIds.isEmpty()) {
            throw new IncorrectMigrationDataException("Not found targetIds for genres with sourceIds: %s"
                .formatted(genreSourceIds));
        }

        List<GenreDocument> genreDocuments = genreRepositoryMongo.findAllById(genreTargetIds);
        if (genreDocuments.isEmpty()) {
            throw new EntityNotFoundException("Not found genres with targetIds: %s"
                .formatted(String.join(",", genreTargetIds)));
        }
        return genreDocuments;
    }

    private List<Long> getGenresIdsFromBook(Book book) {
        if (book.getGenres().isEmpty()) {
            throw new IncorrectDataException("Genres for book with sourceId=%d are empty".formatted(book.getId()));
        }
        List<Long> genreSourceIds = book.getGenres().stream()
            .map(Genre::getId)
            .toList();
        if (genreSourceIds.isEmpty()) {
            throw new IncorrectDataException("Genres Ids for book with sourceId=%d are empty".formatted(book.getId()));
        }
        return genreSourceIds;
    }

    private BookDocument findBookDocForComment(Comment comment, BookRepositoryMongo bookRepositoryMongo) {
        if (comment.getBook() == null) {
            throw new IncorrectDataException("Book for comment with sourceId=%d is null".formatted(comment.getId()));
        }
        long bookSourceId = comment.getBook().getId();
        String bookTargetId = findTargetIdInMigrationTable(BOOKS_ID_TABLE, bookSourceId);
        if (bookTargetId == null) {
            throw new IncorrectMigrationDataException("Not found targetId to book with sourceId=%d"
                .formatted(bookSourceId));
        }
        return bookRepositoryMongo.findById(bookTargetId)
            .orElseThrow(() -> new EntityNotFoundException("Book with targetId=%s not found".formatted(bookTargetId)));
    }

    private <T extends MongoDocument> ItemWriteListener<T> createWriterListener(List<Long> currentSourceIds,
        List<String> currentTargetIds, String tableName,
        String entityName) {
        return new ItemWriteListener<T>() {
            @Override
            public void afterWrite(@Nonnull Chunk<? extends T> documents) {
                for (T document : documents) {
                    currentTargetIds.add(document.getId());
                }
                saveIdsIntoMigratedTable(currentSourceIds, currentTargetIds, tableName, entityName);
            }
        };
    }

    private StepExecutionListener createStepExecutionListener(String entityName) {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(@Nonnull StepExecution stepExecution) {
                log.info("Start {} migration", entityName);
            }

            @Override
            public ExitStatus afterStep(@Nonnull StepExecution stepExecution) {
                log.info("End {} migration. Read {} Write {}", entityName, stepExecution.getReadCount(),
                    stepExecution.getWriteCount());
                return ExitStatus.COMPLETED;
            }
        };
    }

    private <T extends H2Entity> ItemReadListener<T> createItemReadListener(List<Long> currentH2Ids) {
        return new ItemReadListener<T>() {
            @Override
            public void afterRead(@Nonnull T entity) {
                currentH2Ids.add(entity.getId());
            }
        };
    }
}
