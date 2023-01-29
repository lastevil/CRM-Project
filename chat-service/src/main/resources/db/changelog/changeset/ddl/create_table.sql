--liquibase formatted sql
--changeset lidij:create_table_users
create table users (
   id serial primary key,
   nicname varchar(36) not null,
   password varchar(80) not null,
   login varchar(36) not null
);

--changeset lidij:create_table_groups
create table groups (
   id serial primary key,
   title varchar(30) not null unique
);
--changeset lidij:create_table_users_groups
create table users_groups (
   user_id bigint not null references users (id),
   group_id bigint not null references groups (id),
   primary key (user_id, group_id)
);



