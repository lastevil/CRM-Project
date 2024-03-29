--liquibase formatted sql
--changeset lidij:create_table_users
create table if not exists users
(
    id       uuid primary key,
    username varchar(36) unique not null,
    nickname varchar(80)        not null
);
create table if not exists groups
(
    id    bigserial primary key,
    title varchar(30) not null unique
);

--changeset lidij:create_table_users_groups
create table if not exists users_groups
(
    user_id  uuid   not null references users (id),
    group_id bigint not null references groups (id),
    primary key (user_id, group_id)
);



