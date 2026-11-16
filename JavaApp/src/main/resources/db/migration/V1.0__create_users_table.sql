CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    birthday DATE,
    role VARCHAR(20) NOT NULL,
    photo_name VARCHAR(30),
    is_active BOOLEAN,
    delete_date DATE

    );