CREATE TABLE IF NOT EXISTS user_questions(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(75),
    message TEXT,
    is_send BOOLEAN,
    create_at TIMESTAMP
);