alter table users
    add column if not exists username varchar(128) not null;
alter table tickets
    add column if not exists overdue_status varchar(50);