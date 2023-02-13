alter table users
    add column if not exists username varchar(128) not null;