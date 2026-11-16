
INSERT INTO tests (id, title, need_correct_answer_count)
VALUES (0, 'Основы Java: Базовый синтаксис', 2);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (0, 'Какой тип данных используется для хранения целых чисел?', 'ONE_ANSWER', 0);

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (0, 'int', TRUE, 0),
    (1, 'String', FALSE, 0),
    (2, 'double', FALSE, 0),
    (3, 'boolean', FALSE, 0);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (1, 'Какое ключевое слово используется для создания нового объекта?', 'ONE_ANSWER', 0);

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (4, 'new', TRUE, 1),
    (5, 'create', FALSE, 1),
    (6, 'object', FALSE, 1),
    (7, 'init', FALSE, 1);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (2, 'Какие из перечисленных являются примитивными типами в Java?', 'MULTI_ANSWERS', 0);

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (8, 'int', TRUE, 2),
    (9, 'double', TRUE, 2),
    (10, 'String', FALSE, 2),
    (11, 'boolean', TRUE, 2),
    (12, 'List', FALSE, 2);


INSERT INTO tests (id, title, need_correct_answer_count)
VALUES (1, 'Основы SQL: Базовые запросы', 2);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (3, 'Какой оператор используется для выборки данных из таблицы?', 'ONE_ANSWER', 1);

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (13, 'SELECT', TRUE, 3),
    (14, 'INSERT', FALSE, 3),
    (15, 'UPDATE', FALSE, 3),
    (16, 'DELETE', FALSE, 3);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (4, 'Какой оператор используется для фильтрации данных?', 'ONE_ANSWER', 1);

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (17, 'WHERE', TRUE, 4),
    (18, 'HAVING', FALSE, 4),
    (19, 'FILTER', FALSE, 4),
    (20, 'ORDER BY', FALSE, 4);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (5, 'Какие операторы относятся к DDL (Data Definition Language)?', 'MULTI_ANSWERS', 1);

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (21, 'CREATE', TRUE, 5),
    (22, 'ALTER', TRUE, 5),
    (23, 'DROP', TRUE, 5),
    (24, 'INSERT', FALSE, 5),
    (25, 'SELECT', FALSE, 5);