create table if not exists departments(
    id   uuid primary key,
    name varchar(255) not null
);

create table if not exists users
(
    id            uuid primary key,
    first_name    varchar(50) not null,
    last_name     varchar(50) not null,
    department_id uuid not null REFERENCES departments (id)
);

create table if not exists tickets
(
    id uuid primary key,
    title varchar(255) not null,
    status varchar(128) not null,
    assignee_id uuid REFERENCES users (id),
    assignee_department_id uuid not null REFERENCES departments (id),
    reporter_id uuid not null REFERENCES users (id),
    created_at TIMESTAMP not null,
    updated_at TIMESTAMP
);