package ru.otus.hw.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = {"/db/delete-all-from-tables.sql", "/db/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class AbstractServiceImplTest {
}
