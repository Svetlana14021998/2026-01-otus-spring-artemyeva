package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.migration.listener.MigrationItemReadListener;
import ru.otus.hw.migration.listener.MigrationItemWriteListener;
import ru.otus.hw.migration.listener.MigrationStepExecutionListener;
import ru.otus.hw.migration.processor.AuthorMigrationProcessor;
import ru.otus.hw.migration.processor.BookMigrationProcessor;
import ru.otus.hw.migration.processor.CommentMigrationProcessor;
import ru.otus.hw.migration.processor.GenreMigrationProcessor;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.repositories.h2.AuthorRepository;
import ru.otus.hw.repositories.h2.BookRepository;
import ru.otus.hw.repositories.h2.CommentRepository;
import ru.otus.hw.repositories.h2.GenreRepository;
import ru.otus.hw.repositories.mongo.AuthorRepositoryMongo;
import ru.otus.hw.repositories.mongo.BookRepositoryMongo;
import ru.otus.hw.repositories.mongo.CommentRepositoryMongo;
import ru.otus.hw.repositories.mongo.GenreRepositoryMongo;

import java.util.Collections;

@Configuration
@Slf4j
public class JobConfig {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private AuthorMigrationProcessor authorMigrationProcessor;

    @Autowired
    private GenreMigrationProcessor genreMigrationProcessor;

    @Autowired
    private BookMigrationProcessor bookMigrationProcessor;

    @Autowired
    private CommentMigrationProcessor commentMigrationProcessor;

    @Autowired
    private MigrationStepExecutionListener stepExecutionListener;

    @Autowired
    private MigrationItemReadListener<Comment> commentReadListener;

    @Autowired
    private MigrationItemWriteListener<CommentDocument> commentWriteListener;

    @Autowired
    private MigrationItemReadListener<Book> bookReadListener;

    @Autowired
    private MigrationItemWriteListener<BookDocument> bookWriteListener;

    @Autowired
    private MigrationItemReadListener<Genre> genreReadListener;

    @Autowired
    private MigrationItemWriteListener<GenreDocument> genreWriteListener;

    @Autowired
    private MigrationItemReadListener<Author> authorReadListener;

    @Autowired
    private MigrationItemWriteListener<AuthorDocument> authorWriteListener;

    @Bean
    public RepositoryItemReader<Author> authorReader(AuthorRepository repository) {
        return createReader(repository);
    }

    @Bean
    public Step migrateAuthorStep(RepositoryItemReader<Author> reader, AuthorRepositoryMongo authorRepositoryMongo) {
        return new StepBuilder("migrateAuthorStep", jobRepository)
            .<Author, AuthorDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(authorMigrationProcessor)
            .writer(authorRepositoryMongo::saveAll)
            .listener(authorReadListener)
            .listener(authorWriteListener)
            .listener(stepExecutionListener)
            .build();
    }

    @Bean
    public RepositoryItemReader<Genre> genreReader(GenreRepository repository) {
        return createReader(repository);
    }

    @Bean
    public Step migrateGenreStep(RepositoryItemReader<Genre> reader, GenreRepositoryMongo genreRepositoryMongo) {
        return new StepBuilder("migrateGenreStep", jobRepository)
            .<Genre, GenreDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(genreMigrationProcessor)
            .writer(genreRepositoryMongo::saveAll)
            .listener(genreReadListener)
            .listener(genreWriteListener)
            .listener(stepExecutionListener)
            .build();
    }

    @Bean
    public RepositoryItemReader<Book> bookReader(BookRepository repository) {
        return createReader(repository);
    }

    @Bean
    public Step migrateBookStep(RepositoryItemReader<Book> reader, BookRepositoryMongo bookRepositoryMongo) {
        return new StepBuilder("migrateBookStep", jobRepository)
            .<Book, BookDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(bookMigrationProcessor)
            .writer(bookRepositoryMongo::saveAll)
            .listener(bookReadListener)
            .listener(bookWriteListener)
            .listener(stepExecutionListener)
            .build();
    }

    @Bean
    public RepositoryItemReader<Comment> commentReader(CommentRepository repository) {
        return createReader(repository);
    }

    @Bean
    public Step migrateCommentStep(RepositoryItemReader<Comment> reader, CommentRepositoryMongo commentRepositoryMongo) {
        return new StepBuilder("migrateCommentStep", jobRepository)
            .<Comment, CommentDocument>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(commentMigrationProcessor)
            .writer(commentRepositoryMongo::saveAll)
            .listener(commentReadListener)
            .listener(commentWriteListener)
            .listener(stepExecutionListener)
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

    private <T, R extends JpaRepository<T, ?>> RepositoryItemReader<T> createReader(R repository) {
        RepositoryItemReader<T> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }
}
