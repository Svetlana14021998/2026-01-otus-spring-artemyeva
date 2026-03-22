delete from books_genres;
delete from genres;
delete from books;
delete from comments;
delete from authors;

ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE books ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;
ALTER TABLE authors ALTER COLUMN id RESTART WITH 1;
