--liquibase formatted sql
--changeset lidij:create_table_chatroom
create table if not exists chatroom (
   uuid          uuid primary key,
   chatdate      varchar(20) not null,
   message       text,
   status        varchar(25) not null,
   recipient_id  uuid not null references users (uuid),
   sender_id     uuid not null references users (uuid)
);
