--liquibase formatted sql
--changeset lidij:create_table_chatgroup
create table if not exists chatgroup (
   uuid          uuid primary key,
   chatdate      varchar(20) not null,
   message       text,
   status        varchar(25) not null,
   group_id      bigint not null references groups (id),
   sender_id     uuid not null references users (uuid),
   recipient_id  uuid not null references users (uuid)
);