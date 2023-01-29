create table if not exists users
(
    id         uuid primary key,
    first_name varchar(50) not null,
    last_name  varchar(50) not null,
    department_id uuid not null
);

create table if not exists departments(
    id uuid not null,
    name varchar(255) not null
);

create table if not exists tickets
(
    id uuid primary key,
    title varchar(255) not null,
    status varchar(128) not null,
    assignee_id uuid,
    assignee_department_id uuid not null,
    reporter_id uuid not null,
    department_id BIGINT NOT NULL,
    created_at TIMESTAMP not null,
    updated_at TIMESTAMP
);