--liquibase formatted sql
--changeset lidij:create_table_chatgroup_1
create table chatgroup (
   id serial primary key,
   chatdate varchar(20) not null,
   message text,
   status varchar(25) not null,
   group_id bigint references groups (id),
   sender_id bigint references users (id),
   recipient_id bigint references users (id)
);