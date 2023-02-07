CREATE TABLE ticket_departments (
                                    department_id BIGINT NOT NULL PRIMARY KEY,
                                    department_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS users_of_ticket
(
    id            BIGINT PRIMARY KEY,
    first_name    VARCHAR(36) NOT NULL,
    last_name     VARCHAR(36) NOT NULL,
    department_id BIGINT      NOT NULL REFERENCES ticket_departments (department_id)
);
CREATE TABLE IF NOT EXISTS tickets
(
    id    BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(128) NOT NULL,
    description TEXT,
    assignee BIGINT REFERENCES users_of_ticket (id),
    reporter BIGINT REFERENCES users_of_ticket (id) NOT NULL,
    department_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date DATE
);




