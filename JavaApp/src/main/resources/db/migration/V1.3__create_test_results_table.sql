CREATE TABLE test_results (
    id BIGSERIAL PRIMARY KEY,
    test_id BIGINT,
    user_id BIGINT,
    correct_answers INT,
    total_question_count INT,
    percentage NUMERIC(5,2),
    passed_date TIMESTAMP,
    is_passed BOOLEAN,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
);