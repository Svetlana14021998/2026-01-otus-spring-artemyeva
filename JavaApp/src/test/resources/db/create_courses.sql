INSERT INTO courses (id, course_type, description, author_id, test_id)
VALUES (0,
        'OOP',
        'Основы объектно-ориентированного программирования',
        1,
        0);

INSERT INTO theory_materials (id, content, course_id)
VALUES (0,
        '<h1>Основы ООП</h1><p>ООП — парадигма программирования, основанная на объектах и классах. Основные принципы: инкапсуляция, наследование, полиморфизм, абстракция.</p>',
        0);

INSERT INTO courses (id, course_type, description, author_id, test_id)
VALUES (1,
        'SQL',
        'Основы языка SQL и работы с базами данных',
        1,
        1);

INSERT INTO theory_materials (id, content, course_id)
VALUES (1,
        '<h1>Основы SQL</h1><p>SQL — язык структурированных запросов. Основные команды: SELECT, INSERT, UPDATE, DELETE. DDL для создания таблиц, DML для работы с данными.</p>',
        1);

INSERT INTO theory_materials (id, content, course_id)
VALUES (2,
        '<h1>JOIN в SQL</h1><p>JOIN — объединение таблиц. Виды: INNER JOIN, LEFT JOIN, RIGHT JOIN, FULL JOIN. Используются для связывания данных из разных таблиц.</p>',
        1);


INSERT INTO courses (id, course_type, description, author_id, test_id)
VALUES (2,
        'COLLECTIONS',
        'Коллекции Java: List, Set, Map, Queue',
        3,
        null);

INSERT INTO theory_materials (id, content, course_id)
VALUES (3,
        '<h1>Коллекции Java</h1><p>Коллекции — структуры данных для хранения объектов. Интерфейсы: List (список), Set (множество), Map (словарь), Queue (очередь). Основные реализации: ArrayList, HashSet, HashMap.</p>',
        2);