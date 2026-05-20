create table if not exists users
(
    id  bigserial   primary key,
    username  varchar(50) unique,
    password varchar(100)
    );

create table if not exists roles
(
    id  bigserial primary key,
    name  varchar(50) unique
    );

create table if not exists users_roles
(
    user_id bigint,
    role_id bigint,
    primary key (user_id,role_id)
);
