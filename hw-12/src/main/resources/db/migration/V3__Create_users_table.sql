create table if not exists users
(
    id  bigserial,
    username  varchar(50) unique,
    password varchar(100),
    primary key(id)
    );