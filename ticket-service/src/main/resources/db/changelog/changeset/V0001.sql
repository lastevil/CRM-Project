CREATE TABLE ticket_departments
(
    department_id   BIGINT      NOT NULL PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_of_ticket
(
    id            UUID PRIMARY KEY,
    username      varchar(36) unique not null,
    first_name    VARCHAR(36)        NOT NULL,
    last_name     VARCHAR(36)        NOT NULL,
    department_id BIGINT             NOT NULL REFERENCES ticket_departments (department_id)
);

CREATE TABLE IF NOT EXISTS tickets
(
    id            UUID PRIMARY KEY,
    title         VARCHAR(255)                         NOT NULL,
    status        VARCHAR(128)                         NOT NULL,
    description   TEXT,
    assignee      UUID REFERENCES users_of_ticket (id),
    reporter      UUID REFERENCES users_of_ticket (id) NOT NULL,
    department_id BIGINT                               NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    due_date      DATE,
    overdue       VARCHAR(100)
);