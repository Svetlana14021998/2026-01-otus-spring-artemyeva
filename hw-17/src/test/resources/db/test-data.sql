insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

INSERT INTO acl_sid ( principal, sid)
VALUES ( 1, 'user'),
       ( 0, 'ROLE_ADMIN'),
       ( 0, 'ROLE_MANAGER');

INSERT INTO acl_class ( class)
VALUES ( 'ru.otus.hw.models.Book');

INSERT INTO acl_object_identity ( object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES ( 1, 1, NULL, 1, 0),
       ( 1, 2, NULL, 2, 0),
       ( 1, 3, NULL, 2, 0);

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure)
VALUES (1, 1, 1, 1, 1, 1, 1),
       (1, 2, 2, 1, 1, 1, 1),
       (1, 3, 3, 1, 1, 1, 1),
       (2, 1, 2, 1, 1, 1, 1),
       (2, 2, 2, 1, 1, 1, 1),
       (2, 3, 3, 1, 1, 1, 1),
       (3, 1, 2, 1, 1, 1, 1),
       (3, 2, 2, 1, 1, 1, 1),
       (3, 3, 3, 1, 1, 1, 1)
;

