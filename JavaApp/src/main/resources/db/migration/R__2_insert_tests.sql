INSERT INTO tests (id, title, need_correct_answer_count)
VALUES (
    0,
    'Итоговый тест по курсу "Основы ООП"',
    4
       );

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    0,
    'Что такое объектно-ориентированное программирование (ООП)?',
    'ONE_ANSWER',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (0, 'Парадигма программирования, основанная на объектах и классах', TRUE, 0),
    (1, 'Язык программирования', FALSE, 0),
    (2, 'Методология управления проектами', FALSE, 0),
    (3, 'База данных', FALSE, 0);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    1,
    'Что такое инкапсуляция в ООП?',
    'ONE_ANSWER',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (4, 'Механизм скрытия внутреннего состояния объекта', TRUE, 1),
    (5, 'Создание нового класса на основе существующего', FALSE, 1),
    (6, 'Способность объектов вести себя по-разному', FALSE, 1),
    (7, 'Выделение существенных характеристик объекта', FALSE, 1);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    2,
    'Какие модификаторы доступа существуют в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (8, 'private', TRUE, 2),
    (9, 'protected', TRUE, 2),
    (10, 'public', TRUE, 2),
    (11, 'internal', FALSE, 2),
    (12, 'static', FALSE, 2);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    3,
    'Что такое абстракция в ООП?',
    'ONE_ANSWER',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (13, 'Выделение существенных характеристик объекта, игнорируя неважные детали', TRUE, 3),
    (14, 'Скрытие внутреннего состояния объекта', FALSE, 3),
    (15, 'Создание иерархии классов', FALSE, 3),
    (16, 'Переопределение методов', FALSE, 3);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    4,
    'Что такое наследование в ООП?',
    'ONE_ANSWER',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (17, 'Создание нового класса на основе существующего', TRUE, 4),
    (18, 'Способность объектов менять свое поведение', FALSE, 4),
    (19, 'Объединение данных и методов в одном объекте', FALSE, 4),
    (20, 'Скрытие реализации от пользователя', FALSE, 4);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    5,
    'Какие ключевые слова используются для наследования в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (21, 'extends', TRUE, 5),
    (22, 'implements', TRUE, 5),
    (23, 'super', FALSE, 5),
    (24, 'inherit', FALSE, 5),
    (25, 'override', FALSE, 5);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    6,
    'Что такое полиморфизм в ООП?',
    'ONE_ANSWER',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (26, 'Способность объектов с одинаковым интерфейсом вести себя по-разному', TRUE, 6),
    (27, 'Создание новых классов на основе существующих', FALSE, 6),
    (28, 'Скрытие внутреннего состояния объекта', FALSE, 6),
    (29, 'Объединение данных в одном объекте', FALSE, 6);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    7,
    'Какие типы полиморфизма существуют в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    0
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (30, 'Переопределение (Override) - время выполнения', TRUE, 7),
    (31, 'Перегрузка (Overload) - время компиляции', TRUE, 7),
    (32, 'Переименование (Rename)', FALSE, 7),
    (33, 'Перемещение (Move)', FALSE, 7);

INSERT INTO tests (id, title, need_correct_answer_count)
VALUES (
    1,
    'Итоговый тест по курсу "Основы SQL"',
    4
       );

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    8,
    'Что такое DDL (Data Definition Language)?',
    'ONE_ANSWER',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (34, 'Язык определения данных для создания и изменения структуры БД', TRUE, 8),
    (35, 'Язык манипуляции данными для работы с записями', FALSE, 8),
    (36, 'Язык запросов к данным для выборки информации', FALSE, 8),
    (37, 'Язык управления доступом', FALSE, 8);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    9,
    'Какие команды относятся к DDL? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (38, 'CREATE', TRUE, 9),
    (39, 'ALTER', TRUE, 9),
    (40, 'DROP', TRUE, 9),
    (41, 'INSERT', FALSE, 9),
    (42, 'SELECT', FALSE, 9);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    10,
    'Что такое DML (Data Manipulation Language)?',
    'ONE_ANSWER',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (43, 'Язык манипуляции данными для добавления, обновления и удаления', TRUE, 10),
    (44, 'Язык определения данных для создания таблиц', FALSE, 10),
    (45, 'Язык запросов к данным', FALSE, 10),
    (46, 'Язык управления транзакциями', FALSE, 10);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    11,
    'Какие команды относятся к DML? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (47, 'INSERT', TRUE, 11),
    (48, 'UPDATE', TRUE, 11),
    (49, 'DELETE', TRUE, 11),
    (50, 'CREATE', FALSE, 11),
    (51, 'ALTER', FALSE, 11);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    12,
    'Для чего используется оператор SELECT в SQL?',
    'ONE_ANSWER',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (52, 'Для выборки данных из базы данных', TRUE, 12),
    (53, 'Для добавления новых записей', FALSE, 12),
    (54, 'Для обновления существующих записей', FALSE, 12),
    (55, 'Для удаления записей', FALSE, 12);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    13,
    'Какие виды JOIN существуют в SQL? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (56, 'INNER JOIN', TRUE, 13),
    (57, 'LEFT JOIN', TRUE, 13),
    (58, 'RIGHT JOIN', TRUE, 13),
    (59, 'OUTER JOIN', TRUE, 13),
    (60, 'CROSS JOIN', TRUE, 13),
    (61, 'MIDDLE JOIN', FALSE, 13);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    14,
    'Что такое подзапрос (Subquery) в SQL?',
    'ONE_ANSWER',
    1
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (62, 'Запрос, вложенный в другой запрос', TRUE, 14),
    (63, 'Запрос для создания таблиц', FALSE, 14),
    (64, 'Запрос для обновления данных', FALSE, 14),
    (65, 'Запрос для удаления данных', FALSE, 14);


INSERT INTO tests (id, title, need_correct_answer_count)
VALUES (
    2,
    'Итоговый тест по курсу "Коллекции Java"',
    4
       );

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
           15,
           'Что такое коллекции в Java?',
           'ONE_ANSWER',
           2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (66, 'Структуры данных для хранения и обработки групп объектов', TRUE, 15),
    (67, 'Специальные типы данных для чисел', FALSE, 15),
    (68, 'Классы для работы с файлами', FALSE, 15),
    (69, 'Интерфейсы для работы с сетью', FALSE, 15);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    16,
    'Какие интерфейсы являются частью Collection в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (70, 'List', TRUE, 16),
    (71, 'Set', TRUE, 16),
    (72, 'Queue', TRUE, 16),
    (73, 'Map', FALSE, 16),
    (74, 'ArrayList', FALSE, 16);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    17,
    'Какая коллекция допускает дубликаты и сохраняет порядок добавления?',
    'ONE_ANSWER',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (75, 'List', TRUE, 17),
    (76, 'Set', FALSE, 17),
    (77, 'Map', FALSE, 17),
    (78, 'Queue', FALSE, 17);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    18,
    'Какая коллекция не допускает дубликатов и не гарантирует порядок элементов?',
    'ONE_ANSWER',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (79, 'HashSet', TRUE, 18),
    (80, 'ArrayList', FALSE, 18),
    (81, 'LinkedList', FALSE, 18),
    (82, 'HashMap', FALSE, 18);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    19,
    'Какие реализации List существуют в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (83, 'ArrayList', TRUE, 19),
    (84, 'LinkedList', TRUE, 19),
    (85, 'Vector', TRUE, 19),
    (86, 'HashSet', FALSE, 19),
    (87, 'TreeSet', FALSE, 19);


INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    20,
    'Какие реализации Set существуют в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (88, 'HashSet', TRUE, 20),
    (89, 'TreeSet', TRUE, 20),
    (90, 'LinkedHashSet', TRUE, 20),
    (91, 'ArrayList', FALSE, 20),
    (92, 'HashMap', FALSE, 20);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    21,
    'Что такое Map в Java?',
    'ONE_ANSWER',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (93, 'Структура данных, хранящая пары ключ-значение', TRUE, 21),
    (94, 'Упорядоченная коллекция с дубликатами', FALSE, 21),
    (95, 'Коллекция уникальных элементов', FALSE, 21),
    (96, 'Очередь с приоритетом', FALSE, 21);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    22,
    'Какие реализации Map существуют в Java? (Выберите все правильные варианты)',
    'MULTI_ANSWERS',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (97, 'HashMap', TRUE, 22),
    (98, 'TreeMap', TRUE, 22),
    (99, 'LinkedHashMap', TRUE, 22),
    (100, 'HashSet', FALSE, 22),
    (101, 'ArrayList', FALSE, 22);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    23,
    'Для чего используется Queue в Java?',
    'ONE_ANSWER',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (102, 'Для работы с элементами по принципу FIFO (первый пришел - первый ушел)', TRUE, 23),
    (103, 'Для хранения уникальных элементов', FALSE, 23),
    (104, 'Для хранения пар ключ-значение', FALSE, 23),
    (105, 'Для быстрого доступа по индексу', FALSE, 23);

INSERT INTO questions (id, text, question_type, test_id)
VALUES (
    24,
    'Какая коллекция лучше подходит для частой вставки элементов в середину?',
    'ONE_ANSWER',
    2
       );

INSERT INTO answers (id, text, is_correct, question_id) VALUES
    (106, 'LinkedList', TRUE, 24),
    (107, 'ArrayList', FALSE, 24),
    (108, 'HashSet', FALSE, 24),
    (109, 'TreeSet', FALSE, 24);

SELECT setval('tests_id_seq', (SELECT MAX(id) FROM tests) + 1);
SELECT setval('questions_id_seq', (SELECT MAX(id) FROM questions) + 1);
SELECT setval('answers_id_seq', (SELECT MAX(id) FROM answers) + 1);