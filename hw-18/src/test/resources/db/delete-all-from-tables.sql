delete from books_genres;
delete from genres;
delete from books;
delete from comments;
delete from authors;

delete from acl_entry;
delete from acl_object_identity;
delete from acl_class;
delete from acl_sid;

ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE books ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;
ALTER TABLE authors ALTER COLUMN id RESTART WITH 1;

ALTER TABLE acl_entry ALTER COLUMN id RESTART WITH 1;
ALTER TABLE acl_object_identity ALTER COLUMN id RESTART WITH 1;
ALTER TABLE acl_class ALTER COLUMN id RESTART WITH 1;
ALTER TABLE acl_sid ALTER COLUMN id RESTART WITH 1;