INSERT INTO tickets_schema.ticket_departments (department_id, department_name)
VALUES ('1', 'Marketing'),
       ('2', 'QA'),
       ('3', 'R&D');

INSERT INTO tickets_schema.users_of_ticket (id, username, first_name, last_name, department_id)
VALUES ('2e18a2f4-a49d-11ed-ad47-0242ac120002', 'j.joyce@mail.ru', 'James', 'Joyce', '1'),
       ('34f3d3f0-a49d-11ed-ad47-0242ac120002', 'm.proust@mail.ru', 'Marcel', 'Proust', '2'),
       ('394e0a24-a49d-11ed-ad47-0242ac120002', 'l.celine@mail.ru', 'Louis', 'Céline', '3');

INSERT INTO tickets_schema.tickets (id, title, description, status, assignee, reporter, department_id, created_at,
                                    due_date, overdue)
VALUES ('a6efd5d0-a49d-11ed-ad47-0242ac120002', 'Make some some streams of consciousness', 'Write an article about them',
        'Запланировано', '2e18a2f4-a49d-11ed-ad47-0242ac120002', '394e0a24-a49d-11ed-ad47-0242ac120002',
        '1', current_timestamp, '2023-02-10', 'false');
