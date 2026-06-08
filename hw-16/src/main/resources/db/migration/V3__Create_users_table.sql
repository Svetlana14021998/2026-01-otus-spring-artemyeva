create table if not exists users
(
    id  bigserial   primary key,
    username  varchar(50) unique,
    password varchar(100)
    );

CREATE TABLE users_authorities (
   user_id BIGINT,
   authority VARCHAR(255),
   FOREIGN KEY (user_id) REFERENCES users(id)
);
