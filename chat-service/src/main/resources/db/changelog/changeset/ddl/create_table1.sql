--liquibase formatted sql
--changeset lidij:create_table_chatroom
create table chatroom (
   id serial primary key,
   chatdate varchar(20) not null,
   message text,
   status varchar(25) not null,
   recipient_id bigint references users (id),
   sender_id bigint references users (id)
);
