CREATE TABLE tests (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    need_correct_answer_count INTEGER DEFAULT 0
);

CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    course_type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    author_id BIGINT,
    test_id BIGINT UNIQUE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE SET NULL
);

CREATE TABLE theory_materials (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    course_id BIGINT,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    test_id BIGINT NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
);

CREATE TABLE answers (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    question_id BIGINT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);