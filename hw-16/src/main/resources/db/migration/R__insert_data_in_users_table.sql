insert into users(username, password)
values ('admin', '$2a$10$9NO2HFJMDbyqcf75ZD1b9.bDErQLUQ2cxqhXyT6w3pZZxb.ZxPywO'),
       ('user', '$2a$10$Isqk0cBKpQiAOX3Eggn3SeURVG3rSrRzmGUwW5ZgXXdaYlz04CtMK'),
       ('manager', '$2a$10$yp7jL4KYjNLYu.Yx7i7CtO5ZgFCqZXLncdGruQg0iULKq0tahXQlW'),
       ('user2', '$2a$10$re9j30JYdb.CmcCliSNe9OlwRr.VB0n22QL6vmacYikkehd/XgDce');

insert into users_authorities (user_id,authority)
values (1,'ROLE_ADMIN'),
       (2,'ROLE_USER'),
       (3,'ROLE_MANAGER'),
       (4,'ROLE_USER');