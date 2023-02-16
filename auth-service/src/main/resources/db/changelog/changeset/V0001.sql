CREATE SCHEMA IF NOT EXISTS auth_schema;
create table departments
(
    id         bigserial primary key,
    title      varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table users
(
    uuid          uuid primary key,
    username      varchar(36) unique not null,
    first_name    varchar(36) not null,
    last_name     varchar(56) not null,
    password      varchar(255) not null,
    email         varchar(50) unique,
    department_id bigint references departments (id),
    created_at    timestamp default current_timestamp,
    updated_at    timestamp default current_timestamp,
    status        varchar(25) not null
);

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table users_roles
(
    user_id    uuid not null references users (uuid),
    role_id    bigint not null references roles (id),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    primary key (user_id, role_id)
);