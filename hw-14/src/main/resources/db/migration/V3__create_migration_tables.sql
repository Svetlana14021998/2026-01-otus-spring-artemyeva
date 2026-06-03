create table if not exists authors_id_mapping (
    source_id BIGINT NOT NULL,
    target_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (source_id));

create table if not exists genres_id_mapping (
    source_id BIGINT NOT NULL,
    target_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (source_id));

create table if not exists books_id_mapping (
    source_id BIGINT NOT NULL,
    target_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (source_id));

create table if not exists comments_id_mapping (
    source_id BIGINT NOT NULL,
    target_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (source_id));