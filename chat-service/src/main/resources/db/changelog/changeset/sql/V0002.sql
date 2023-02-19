--liquibase formatted sql
--changeset lidij:create_table_chatroom
create table if not exists chatroom (
   id          uuid primary key,
   chatdate      varchar(20) not null,
   message       text,
   status        varchar(25) not null,
   recipient_id  uuid not null references users (id),
   sender_id     uuid not null references users (id)
);
