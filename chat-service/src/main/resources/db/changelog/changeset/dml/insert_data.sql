--liquibase formatted sql
--changeset lidij:add_data_group
insert into groups (title)
values ('ООО UniCRM');
