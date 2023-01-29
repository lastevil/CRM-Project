--liquibase formatted sql
--changeset lidij:add_data_group_1
insert into groups (title)
values ('Общий чат'),
       ('Группа 1'),
       ('Группа 2');