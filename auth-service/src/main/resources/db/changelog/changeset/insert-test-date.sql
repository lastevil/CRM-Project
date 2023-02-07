insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into departments (title)
values ('System Administration'),
       ('Superiors'),
       ('Management');

insert into users (username, first_name, password, department_id, status)
values ('admin1', 'admin1', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 1, 'ACTIVE'),
       ('admin2', 'admin2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 1, 'NOT_ACTIVE'),
       ('user1', 'user1', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 3, 'NOT_ACTIVE');

insert into users_roles (user_id, role_id)
values (1, 2),
       (2, 2),
       (3, 1);