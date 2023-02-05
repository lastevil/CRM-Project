INSERT INTO tickets_schema.ticket_departments (department_id, department_name)
VALUES ('cef5b424-a49c-11ed-a8fc-0242ac120002', 'Marketing'),
       ('f62083ee-a49c-11ed-a8fc-0242ac120002', 'QA'),
       ('02c9f21a-a49d-11ed-a8fc-0242ac120002', 'R&D');

INSERT INTO tickets_schema.users_of_ticket (id, first_name, last_name, department_id)
VALUES ('2e18a2f4-a49d-11ed-ad47-0242ac120002', 'James', 'Joyce', '02c9f21a-a49d-11ed-a8fc-0242ac120002'),
       ('34f3d3f0-a49d-11ed-ad47-0242ac120002', 'Marcel', 'Proust', 'f62083ee-a49c-11ed-a8fc-0242ac120002'),
       ('394e0a24-a49d-11ed-ad47-0242ac120002', 'Louis', 'Céline', 'cef5b424-a49c-11ed-a8fc-0242ac120002');

INSERT INTO tickets_schema.tickets (id, title, description, status, assignee, reporter, department_id, created_at,
                                    due_date)
VALUES ('a6efd5d0-a49d-11ed-ad47-0242ac120002', 'Make some some streams of consciousness', 'Write an article about them',
        'Запланировано', '2e18a2f4-a49d-11ed-ad47-0242ac120002', '394e0a24-a49d-11ed-ad47-0242ac120002',
        '02c9f21a-a49d-11ed-a8fc-0242ac120002', current_timestamp, '2023-02-10');
