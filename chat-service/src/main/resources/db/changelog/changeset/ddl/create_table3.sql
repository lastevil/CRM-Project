--liquibase formatted sql
--changeset lidij:create_table_groups_1
create table if not exists groups (
   id bigserial primary key,
   title varchar(30) not null unique
);