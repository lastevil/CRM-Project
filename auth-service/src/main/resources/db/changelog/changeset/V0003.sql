INSERT INTO users
values ('99e61aa4-b458-42ca-9282-5cb0f097de60',
        'Admin',
        'admin',
        'local',
        '$2a$10$Lz2w6ewuLyyJQRQlnsSW5ue03TKXMv3GO2JSYXM5vZuHSK71E8ley',
        'admin@local.org',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 'ACTIVE');
INSERT INTO users_roles
values ('99e61aa4-b458-42ca-9282-5cb0f097de60',
                     2,
                     CURRENT_TIMESTAMP,
                     CURRENT_TIMESTAMP)
;