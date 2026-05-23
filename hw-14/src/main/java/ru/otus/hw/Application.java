package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.sql.SQLException;

@EnableMongoRepositories
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws SQLException {
        //для отладки
        // Console.main(args);
        SpringApplication.run(Application.class, args);
    }
}
