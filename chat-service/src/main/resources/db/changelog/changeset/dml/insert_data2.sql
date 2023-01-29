--liquibase formatted sql
--changeset lidij:add_data_users_groups_1
insert into users_groups (user_id, group_id)
values (1,1),
       (1,2),
       (1,3),
       (2,1),
       (3,1),
       (3,3),
       (4,1),
       (4,3),
       (5,1),
       (6,1),
       (7,1),
       (7,2),
       (7,3),
       (8,1),
       (9,1),
       (10,1);