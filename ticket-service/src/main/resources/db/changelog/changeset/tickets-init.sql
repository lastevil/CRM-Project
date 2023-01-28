CREATE TABLE IF NOT EXISTS users_of_ticket
(
    id            BIGINT PRIMARY KEY,
    first_name    VARCHAR(36) NOT NULL,
    last_name     VARCHAR(36) NOT NULL,
    department_id BIGINT      NOT NULL,
    user_role_id  BIGINT      not null
);

CREATE TABLE IF NOT EXISTS tickets
(
    id       BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    status VARCHAR(128),
    description TEXT,
    assignee BIGINT REFERENCES users_of_ticket (id),
    reporter BIGINT REFERENCES users_of_ticket (id),
    department_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date DATE
);




