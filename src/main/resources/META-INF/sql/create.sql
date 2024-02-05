create schema if not exists management;

create table if not exists management.users
(
    id       serial not null primary key,
    name     text,
    email    text,
    password text
);

create unique index if not exists users_id_uindex
    on management.users (id);

create table if not exists management.airports
(
    id          serial not null primary key,
    name        text,
    code        text,
    city        text,
    created_by  integer,
    create_date date
);

create unique index if not exists airports_id_uindex
    on management.airports (id);

