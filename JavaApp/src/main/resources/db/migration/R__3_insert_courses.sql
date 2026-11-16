INSERT INTO courses (id, course_type, description, author_id,test_id)
VALUES (
           0,
           'OOP',
           'Изучение основ объектно-ориентированного программирования: Инкапсуляция, Абстракция, Наследование и Полиморфизм',
           4,
        0
       );

INSERT INTO theory_materials (id, content, course_id)
VALUES
    (
        1,
        '<h1>Введение в объектно-ориентированное программирование</h1>
        <p><strong>Объектно-ориентированное программирование (ООП)</strong> — это парадигма программирования, в которой основными концепциями являются объекты и классы.</p>

        <h2>Что такое объект?</h2>
        <p><strong>Объект</strong> — это сущность, которая имеет состояние (данные) и поведение (методы).</p>

        <h2>Четыре основных принципа ООП:</h2>
        <ul>
            <li><strong>Инкапсуляция</strong> — скрытие внутреннего состояния</li>
            <li><strong>Абстракция</strong> — выделение существенных характеристик</li>
            <li><strong>Наследование</strong> — создание новых классов на основе существующих</li>
            <li><strong>Полиморфизм</strong> — способность объектов вести себя по-разному</li>
        </ul>',
        0
    ),
    (
        2,
        '<h1>Инкапсуляция</h1>
        <p><strong>Инкапсуляция</strong> — это механизм, который объединяет данные и методы работы с этими данными в одном объекте и скрывает внутреннее состояние объекта от внешнего мира.</p>

        <h3>Пример инкапсуляции:</h3>
        <pre><code>
        public class BankAccount {
            private double balance;  // Приватное поле

            public void deposit(double amount) {
                if (amount > 0) {
                    balance += amount;
                }
            }

            public double getBalance() {
                return balance;
            }
        }
        </code></pre>

        <h3>Модификаторы доступа в Java:</h3>
        <ul>
            <li><strong>private</strong> — доступ только внутри класса</li>
            <li><strong>default</strong> — доступ внутри пакета</li>
            <li><strong>protected</strong> — доступ внутри пакета и наследникам</li>
            <li><strong>public</strong> — доступ отовсюду</li>
        </ul>',
        0
    ),
    (
        3,
        '<h1>Абстракция</h1>
        <p><strong>Абстракция</strong> — это принцип, который позволяет выделить существенные характеристики объекта, игнорируя неважные детали.</p>

        <h3>Пример абстрактного класса:</h3>
        <pre><code>
        public abstract class Animal {
            protected String name;

            public Animal(String name) {
                this.name = name;
            }

            public abstract void makeSound();
            public abstract void move();
        }

        public class Dog extends Animal {
            public Dog(String name) {
                super(name);
            }

            @Override
            public void makeSound() {
                System.out.println("Гав-гав!");
            }

            @Override
            public void move() {
                System.out.println("Бегает");
            }
        }
        </code></pre>',
        0
    ),
    (
        4,
        '<h1>Наследование</h1>
        <p><strong>Наследование</strong> — это механизм, который позволяет создавать новый класс на основе существующего, расширяя или переопределяя его функциональность.</p>

        <h3>Пример наследования:</h3>
        <pre><code>
        // Родительский класс
        public class Vehicle {
            protected String brand;
            protected int year;

            public void start() {
                System.out.println("Транспортное средство запущено");
            }
        }

        // Дочерний класс
        public class Car extends Vehicle {
            private int numberOfDoors;

            @Override
            public void start() {
                System.out.println("Автомобиль " + brand + " завелся");
            }
        }
        </code></pre>

        <h3>Ключевые слова:</h3>
        <ul>
            <li><strong>extends</strong> — для наследования классов</li>
            <li><strong>super</strong> — для вызова конструктора или методов родителя</li>
            <li><strong>@Override</strong> — аннотация для переопределения методов</li>
        </ul>',
        0
    ),
    (
        5,
        '<h1>Полиморфизм</h1>
        <p><strong>Полиморфизм</strong> — это способность объектов с одинаковым интерфейсом вести себя по-разному в зависимости от реализации.</p>

        <h3>Пример полиморфизма:</h3>
        <pre><code>
        // Родительский класс
        public class Shape {
            public double calculateArea() {
                return 0;
            }
        }

        // Дочерние классы
        public class Circle extends Shape {
            private double radius;

            @Override
            public double calculateArea() {
                return Math.PI * radius * radius;
            }
        }

        public class Rectangle extends Shape {
            private double width;
            private double height;

            @Override
            public double calculateArea() {
                return width * height;
            }
        }

        // Использование
        Shape[] shapes = {new Circle(5), new Rectangle(4, 6)};
        for (Shape shape : shapes) {
            System.out.println("Площадь: " + shape.calculateArea());
        }
        </code></pre>

        <h3>Два типа полиморфизма:</h3>
        <ul>
            <li><strong>Переопределение (Override)</strong> — время выполнения</li>
            <li><strong>Перегрузка (Overload)</strong> — время компиляции</li>
        </ul>',
        0
    );

INSERT INTO courses (id, course_type, description, author_id,test_id)
VALUES (
           1,
           'SQL',
           'Краткий обзор по базовым видам SQL запросов: DDL, DML, DQL, JOIN и подзапросы с примерами и подробными объяснениями',
           2,
        1
       );

INSERT INTO theory_materials (id, content, course_id)
VALUES
    (
        6,
        '<h1>DDL — Data Definition Language (Язык определения данных)</h1>

        <p><strong>DDL (Data Definition Language)</strong> — это подмножество SQL, которое используется для создания и изменения структуры базы данных и её объектов.</p>

        <h2>Основные команды DDL:</h2>

        <h3>1. CREATE — создание объектов</h3>
        <p>Используется для создания новых объектов в базе данных: таблиц, индексов, представлений, процедур и т.д.</p>

        <h4>Создание таблицы:</h4>
        <pre><code>
    -- Создание таблицы пользователей
    CREATE TABLE users (
        id INT PRIMARY KEY AUTO_INCREMENT,
        username VARCHAR(50) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        age INT CHECK (age >= 0 AND age <= 150),
        active BOOLEAN DEFAULT TRUE,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

    -- Создание таблицы заказов
    CREATE TABLE orders (
        id INT PRIMARY KEY AUTO_INCREMENT,
        user_id INT NOT NULL,
        total DECIMAL(10,2) DEFAULT 0.00,
        status VARCHAR(20) DEFAULT "pending",
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
        </code></pre>

        <h4>Создание индекса:</h4>
        <pre><code>
    -- Создание индекса для ускорения поиска
    CREATE INDEX idx_username ON users(username);
    CREATE INDEX idx_email ON users(email);
    CREATE INDEX idx_user_id ON orders(user_id);
        </code></pre>

        <h4>Создание представления:</h4>
        <pre><code>
    -- Создание представления для активных пользователей
    CREATE VIEW active_users AS
    SELECT id, username, email, age
    FROM users
    WHERE active = TRUE;

    -- Использование представления
    SELECT * FROM active_users;
        </code></pre>

        <h3>2. ALTER — изменение структуры</h3>
        <p>Используется для изменения структуры существующих объектов.</p>

        <pre><code>
    -- Добавление новой колонки
    ALTER TABLE users ADD COLUMN phone VARCHAR(20);

    -- Изменение типа колонки
    ALTER TABLE users MODIFY COLUMN age TINYINT;

    -- Переименование колонки
    ALTER TABLE users RENAME COLUMN age TO user_age;

    -- Добавление ограничения
    ALTER TABLE users ADD CONSTRAINT chk_age CHECK (user_age >= 0);

    -- Удаление колонки
    ALTER TABLE users DROP COLUMN phone;
        </code></pre>

        <h3>3. DROP — удаление объектов</h3>
        <p>Используется для полного удаления объектов из базы данных.</p>

        <pre><code>
    -- Удаление таблицы
    DROP TABLE users;

    -- Удаление базы данных
    DROP DATABASE my_database;

    -- Удаление индекса
    DROP INDEX idx_username ON users;

    -- Удаление представления
    DROP VIEW active_users;
        </code></pre>

        <h3>4. TRUNCATE — очистка таблицы</h3>
        <p>Быстрое удаление всех данных из таблицы (быстрее DELETE).</p>

        <pre><code>
    -- Очистка таблицы (сброс автоинкремента)
    TRUNCATE TABLE users;

    -- Отличие от DELETE:
    -- DELETE — удаляет по строкам, медленнее, можно откатить
    -- TRUNCATE — удаляет все сразу, быстрее, нельзя откатить
        </code></pre>

        <h3>Ограничения (Constraints):</h3>
        <ul>
            <li><strong>PRIMARY KEY</strong> — уникальный идентификатор записи</li>
            <li><strong>FOREIGN KEY</strong> — связь с другой таблицей</li>
            <li><strong>UNIQUE</strong> — уникальное значение в колонке</li>
            <li><strong>NOT NULL</strong> — обязательное поле</li>
            <li><strong>CHECK</strong> — условие для значений</li>
            <li><strong>DEFAULT</strong> — значение по умолчанию</li>
        </ul>

        <h3>Пример полного CREATE TABLE со всеми ограничениями:</h3>
        <pre><code>
    CREATE TABLE products (
        id INT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(100) NOT NULL,
        description TEXT,
        price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
        category_id INT,
        stock INT DEFAULT 0 CHECK (stock >= 0),
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        UNIQUE KEY unique_name (name),
        FOREIGN KEY (category_id) REFERENCES categories(id)
    );
        </code></pre>',
        1
    ),
    (
        7,
        '<h1>DML — Data Manipulation Language (Язык манипуляции данными)</h1>

        <p><strong>DML (Data Manipulation Language)</strong> — это подмножество SQL для работы с данными в таблицах: добавление, обновление и удаление записей.</p>

        <h2>Основные команды DML:</h2>

        <h3>1. INSERT — добавление данных</h3>
        <p>Используется для вставки новых записей в таблицу.</p>

        <h4>Базовый синтаксис:</h4>
        <pre><code>
    -- Добавление одной записи
    INSERT INTO users (username, email, password, age)
    VALUES ("john_doe", "john@example.com", "pass123", 25);

    -- Добавление записи с указанием только некоторых полей
    INSERT INTO users (username, email, password)
    VALUES ("alice", "alice@example.com", "pass456");
    -- age будет NULL, active будет TRUE (значение по умолчанию)
        </code></pre>

        <h4>Добавление нескольких записей:</h4>
        <pre><code>
    -- Массовая вставка
    INSERT INTO users (username, email, password, age) VALUES
    ("bob", "bob@example.com", "pass789", 30),
    ("charlie", "charlie@example.com", "pass101", 28),
    ("diana", "diana@example.com", "pass202", 35),
    ("eve", "eve@example.com", "pass303", 22);

    -- Вставка с возвратом ID (MySQL)
    INSERT INTO users (username, email, password)
    VALUES ("frank", "frank@example.com", "pass404");
    SELECT LAST_INSERT_ID(); -- Получить ID последней вставленной записи
        </code></pre>

        <h4>INSERT SELECT — вставка из другой таблицы:</h4>
        <pre><code>
    -- Копирование данных из одной таблицы в другую
    INSERT INTO archived_users (username, email, age, deleted_at)
    SELECT username, email, age, NOW()
    FROM users
    WHERE active = FALSE;
        </code></pre>

        <h3>2. UPDATE — обновление данных</h3>
        <p>Используется для изменения существующих записей.</p>

        <h4>Базовый синтаксис:</h4>
        <pre><code>
    -- Обновление конкретной записи
    UPDATE users
    SET email = "new_email@example.com", updated_at = NOW()
    WHERE id = 1;

    -- Обновление нескольких записей
    UPDATE users
    SET age = age + 1
    WHERE age < 18;

    -- Обновление с вычислением
    UPDATE products
    SET price = price * 1.1, updated_at = NOW()
    WHERE category_id = 1;
        </code></pre>

        <h4>Обновление с подзапросом:</h4>
        <pre><code>
    -- Обновление на основе данных из другой таблицы
    UPDATE orders
    SET status = "processing"
    WHERE user_id IN (
        SELECT id FROM users
        WHERE username LIKE "john%"
    );

    -- Обновление с JOIN (в некоторых СУБД)
    UPDATE products p
    JOIN categories c ON p.category_id = c.id
    SET p.price = p.price * 1.2
    WHERE c.name = "Electronics";
        </code></pre>

        <h4>Осторожно! UPDATE без WHERE:</h4>
        <pre><code>
    -- ⚠️ ОПАСНО! Обновит все записи
    UPDATE users SET active = FALSE;
    -- Если забыли WHERE, обновятся все пользователи!

    -- ✅ Правильно: всегда указывайте условие
    UPDATE users SET active = FALSE WHERE id = 5;
        </code></pre>

        <h3>3. DELETE — удаление данных</h3>
        <p>Используется для удаления записей из таблицы.</p>

        <h4>Базовый синтаксис:</h4>
        <pre><code>
    -- Удаление конкретной записи
    DELETE FROM users WHERE id = 5;

    -- Удаление нескольких записей
    DELETE FROM users WHERE age > 65;

    -- Удаление с подзапросом
    DELETE FROM orders
    WHERE user_id IN (
        SELECT id FROM users WHERE active = FALSE
    );
        </code></pre>

        <h4>DELETE с JOIN:</h4>
        <pre><code>
    -- Удаление записей с JOIN (в некоторых СУБД)
    DELETE FROM orders
    WHERE user_id IN (
        SELECT id FROM users WHERE created_at < "2020-01-01"
    );
        </code></pre>

        <h4>Осторожно! DELETE без WHERE:</h4>
        <pre><code>
    -- ⚠️ ОПАСНО! Удалит все записи
    DELETE FROM users;
    -- Все пользователи будут удалены!

    -- ✅ Правильно: всегда указывайте условие
    DELETE FROM users WHERE active = FALSE;
        </code></pre>

        <h2>Транзакции для групповых операций:</h2>
        <pre><code>
    -- START TRANSACTION — начало транзакции
    START TRANSACTION;

    -- Выполняем операции
    UPDATE accounts SET balance = balance - 100 WHERE user_id = 1;
    UPDATE accounts SET balance = balance + 100 WHERE user_id = 2;

    -- Проверяем результат
    SELECT * FROM accounts WHERE user_id IN (1, 2);

    -- COMMIT — подтверждаем изменения
    COMMIT;

    -- Или ROLLBACK — откатываем в случае ошибки
    ROLLBACK;
        </code></pre>

        <h2>Лучшие практики:</h2>
        <ul>
            <li>✅ Всегда используйте WHERE в UPDATE и DELETE</li>
            <li>✅ Проверяйте данные через SELECT перед UPDATE/DELETE</li>
            <li>✅ Используйте транзакции для связанных операций</li>
            <li>✅ Создавайте резервные копии перед массовыми операциями</li>
            <li>✅ Используйте EXPLAIN для проверки плана выполнения</li>
        </ul>',
        1
    ),
    (
        8,
        '<h1>DQL — Data Query Language (Язык запросов к данным)</h1>

        <p><strong>DQL (Data Query Language)</strong> — это подмножество SQL, которое используется для выборки данных из базы данных. Основная команда — <strong>SELECT</strong>.</p>

        <h2>Базовый синтаксис SELECT:</h2>
        <pre><code>
    SELECT [DISTINCT] колонки
    FROM таблица
    [WHERE условие]
    [GROUP BY колонка]
    [HAVING условие_группы]
    [ORDER BY колонка ASC|DESC]
    [LIMIT количество]
    [OFFSET смещение];
        </code></pre>

        <h3>1. Простые запросы SELECT</h3>

        <h4>Выборка всех колонок:</h4>
        <pre><code>
    -- Выборка всех данных из таблицы
    SELECT * FROM users;

    -- Выборка с условием
    SELECT * FROM users WHERE age >= 18;
        </code></pre>

        <h4>Выборка конкретных колонок:</h4>
        <pre><code>
    -- Выборка только нужных полей
    SELECT id, username, email FROM users;

    -- С алиасами (псевдонимами)
    SELECT id AS user_id, username AS login FROM users;
        </code></pre>

        <h4>Фильтрация с WHERE:</h4>
        <pre><code>
    -- Операторы сравнения
    SELECT * FROM users WHERE age = 25;
    SELECT * FROM users WHERE age != 25;
    SELECT * FROM users WHERE age > 18;
    SELECT * FROM users WHERE age >= 18;
    SELECT * FROM users WHERE age BETWEEN 18 AND 30;

    -- LIKE — поиск по шаблону
    SELECT * FROM users WHERE username LIKE "a%";      -- начинается с a
    SELECT * FROM users WHERE username LIKE "%son";    -- заканчивается на son
    SELECT * FROM users WHERE email LIKE "%@gmail.com"; -- заканчивается на @gmail.com
    SELECT * FROM users WHERE username LIKE "_o%";     -- вторая буква o

    -- IN — список значений
    SELECT * FROM users WHERE department IN ("IT", "HR", "Finance");
    SELECT * FROM users WHERE age IN (25, 30, 35);

    -- NULL
    SELECT * FROM users WHERE phone IS NULL;
    SELECT * FROM users WHERE phone IS NOT NULL;

    -- Комбинирование условий
    SELECT * FROM users
    WHERE age >= 18
    AND city = "Moscow"
    AND active = TRUE;

    SELECT * FROM users
    WHERE age < 18
    OR age > 65;
        </code></pre>

        <h3>2. Сортировка и ограничение</h3>

        <h4>ORDER BY — сортировка:</h4>
        <pre><code>
    -- По возрастанию (ASC — по умолчанию)
    SELECT * FROM users ORDER BY age;

    -- По убыванию
    SELECT * FROM users ORDER BY age DESC;

    -- Сортировка по нескольким колонкам
    SELECT * FROM users ORDER BY city ASC, age DESC;

    -- Сортировка по выражению
    SELECT * FROM users ORDER BY LENGTH(username) DESC;
        </code></pre>

        <h4>LIMIT — ограничение количества записей:</h4>
        <pre><code>
    -- Первые 10 записей
    SELECT * FROM users LIMIT 10;

    -- Записи с 11 по 20 (пагинация)
    SELECT * FROM users LIMIT 10 OFFSET 10;
    -- или
    SELECT * FROM users LIMIT 10, 10;
        </code></pre>

        <h3>3. Агрегатные функции</h3>

        <pre><code>
    -- COUNT — количество записей
    SELECT COUNT(*) FROM users;
    SELECT COUNT(DISTINCT age) FROM users;
    SELECT COUNT(id) FROM users WHERE active = TRUE;

    -- SUM — сумма
    SELECT SUM(price) FROM products;
    SELECT SUM(quantity * price) FROM orders;

    -- AVG — среднее значение
    SELECT AVG(age) FROM users;
    SELECT AVG(price) FROM products WHERE category_id = 1;

    -- MIN/MAX — минимум и максимум
    SELECT MIN(price) AS min_price, MAX(price) AS max_price FROM products;

    -- GROUP BY — группировка
    SELECT department, COUNT(*) AS employee_count
    FROM employees
    GROUP BY department;

    -- Группировка с несколькими полями
    SELECT department, city, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department, city;

    -- HAVING — фильтрация групп
    SELECT department, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department
    HAVING AVG(salary) > 50000;
        </code></pre>

        <h3>4. DISTINCT — уникальные значения</h3>
        <pre><code>
    -- Уникальные города
    SELECT DISTINCT city FROM users;

    -- Уникальные комбинации
    SELECT DISTINCT city, age FROM users;
        </code></pre>

        <h3>5. Комбинированные запросы</h3>
        <pre><code>
    -- Топ-5 самых молодых пользователей
    SELECT username, age
    FROM users
    WHERE active = TRUE
    ORDER BY age ASC
    LIMIT 5;

    -- Средний возраст по городам с количеством людей больше 10
    SELECT city, AVG(age) AS avg_age, COUNT(*) AS count
    FROM users
    GROUP BY city
    HAVING COUNT(*) >= 10
    ORDER BY avg_age DESC;
        </code></pre>',
        1
    ),
    (
        9,
        '<h1>JOIN — Объединение таблиц</h1>

        <p><strong>JOIN</strong> — это оператор SQL, который используется для объединения данных из нескольких таблиц на основе связей между ними.</p>

        <h2>Виды JOIN:</h2>

        <h3>1. INNER JOIN (внутреннее объединение)</h3>
        <p>Возвращает только те записи, которые имеют совпадения в обеих таблицах.</p>

        <pre><code>
    -- Базовый INNER JOIN
    SELECT users.username, orders.total, orders.status
    FROM users
    INNER JOIN orders ON users.id = orders.user_id;

    -- Сокращенная запись
    SELECT u.username, o.total, o.status
    FROM users u
    JOIN orders o ON u.id = o.user_id;

    -- JOIN с несколькими условиями
    SELECT u.username, o.total, p.name
    FROM users u
    JOIN orders o ON u.id = o.user_id
    JOIN products p ON o.product_id = p.id;
        </code></pre>

        <h4>Пример с тремя таблицами:</h4>
        <pre><code>
    -- Полная информация о заказе
    SELECT
        u.username,
        u.email,
        o.total,
        o.status,
        p.name AS product_name,
        oi.quantity
    FROM users u
    INNER JOIN orders o ON u.id = o.user_id
    INNER JOIN order_items oi ON o.id = oi.order_id
    INNER JOIN products p ON oi.product_id = p.id
    WHERE o.status = "completed";
        </code></pre>

        <h3>2. LEFT JOIN (левое объединение)</h3>
        <p>Возвращает все записи из левой таблицы и совпадающие из правой. Если совпадений нет — NULL.</p>

        <pre><code>
    -- Все пользователи и их заказы
    SELECT u.username, o.total, o.status
    FROM users u
    LEFT JOIN orders o ON u.id = o.user_id;

    -- Пользователи без заказов
    SELECT u.username, o.total
    FROM users u
    LEFT JOIN orders o ON u.id = o.user_id
    WHERE o.id IS NULL;
        </code></pre>

        <h3>3. RIGHT JOIN (правое объединение)</h3>
        <p>Возвращает все записи из правой таблицы и совпадающие из левой.</p>

        <pre><code>
    -- Все заказы и пользователи (если есть)
    SELECT u.username, o.total
    FROM users u
    RIGHT JOIN orders o ON u.id = o.user_id;
        </code></pre>

        <h3>4. FULL OUTER JOIN (полное объединение)</h3>
        <p>Возвращает все записи из обеих таблиц.</p>

        <pre><code>
    -- PostgreSQL
    SELECT u.username, o.total
    FROM users u
    FULL OUTER JOIN orders o ON u.id = o.user_id;

    -- MySQL (эмуляция через UNION)
    SELECT u.username, o.total
    FROM users u
    LEFT JOIN orders o ON u.id = o.user_id
    UNION
    SELECT u.username, o.total
    FROM users u
    RIGHT JOIN orders o ON u.id = o.user_id;
        </code></pre>

        <h3>5. CROSS JOIN (перекрестное объединение)</h3>
        <p>Декартово произведение таблиц.</p>

        <pre><code>
    -- Все возможные комбинации пользователей и продуктов
    SELECT u.username, p.name
    FROM users u
    CROSS JOIN products p;
        </code></pre>

        <h3>6. SELF JOIN (самообъединение)</h3>
        <p>Присоединение таблицы к самой себе.</p>

        <pre><code>
    -- Сотрудники и их менеджеры
    SELECT
        e.name AS employee,
        m.name AS manager
    FROM employees e
    LEFT JOIN employees m ON e.manager_id = m.id;
        </code></pre>

        <h2>Практические примеры JOIN:</h2>

        <h3>Анализ продаж:</h3>
        <pre><code>
    -- Топ-5 пользователей по сумме заказов
    SELECT
        u.username,
        COUNT(o.id) AS orders_count,
        SUM(o.total) AS total_spent
    FROM users u
    JOIN orders o ON u.id = o.user_id
    WHERE o.status = "completed"
    GROUP BY u.id
    ORDER BY total_spent DESC
    LIMIT 5;
        </code></pre>

        <h3>Статистика по категориям:</h3>
        <pre><code>
    -- Количество продуктов по категориям
    SELECT
        c.name AS category,
        COUNT(p.id) AS products_count,
        AVG(p.price) AS avg_price
    FROM categories c
    LEFT JOIN products p ON c.id = p.category_id
    GROUP BY c.id
    ORDER BY products_count DESC;
        </code></pre>

        <h3>JOIN с условиями:</h3>
        <pre><code>
    -- Заказы за последний месяц с информацией о пользователях
    SELECT
        u.username,
        u.email,
        o.total,
        o.created_at
    FROM users u
    INNER JOIN orders o ON u.id = o.user_id
    WHERE o.created_at >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
    ORDER BY o.created_at DESC;
        </code></pre>',
        1
    ),
    (
        10,
        '<h1>Подзапросы и продвинутые конструкции SQL</h1>

        <p><strong>Подзапрос (Subquery)</strong> — это запрос, вложенный в другой запрос. Используется для выполнения сложных операций выборки данных.</p>

        <h2>Виды подзапросов:</h2>

        <h3>1. Подзапрос в WHERE</h3>
        <pre><code>
    -- Пользователи старше среднего возраста
    SELECT * FROM users
    WHERE age > (SELECT AVG(age) FROM users);

    -- Продукты дороже среднего в своей категории
    SELECT p.name, p.price, p.category_id
    FROM products p
    WHERE p.price > (
        SELECT AVG(price)
        FROM products
        WHERE category_id = p.category_id
    );
        </code></pre>

        <h3>2. Подзапрос в SELECT</h3>
        <pre><code>
    -- Количество заказов для каждого пользователя
    SELECT
        u.username,
        (SELECT COUNT(*) FROM orders WHERE user_id = u.id) AS orders_count
    FROM users u;

    -- Сравнение зарплаты со средней по отделу
    SELECT
        e.name,
        e.salary,
        (SELECT AVG(salary) FROM employees WHERE department = e.department) AS dept_avg,
        e.salary - (SELECT AVG(salary) FROM employees WHERE department = e.department) AS difference
    FROM employees e;
        </code></pre>

        <h3>3. Подзапрос в FROM</h3>
        <pre><code>
    -- Использование подзапроса как таблицы
    SELECT
        dept_stats.department,
        dept_stats.avg_salary,
        dept_stats.employee_count
    FROM (
        SELECT
            department,
            AVG(salary) AS avg_salary,
            COUNT(*) AS employee_count
        FROM employees
        GROUP BY department
    ) AS dept_stats
    WHERE dept_stats.avg_salary > 50000;
        </code></pre>

        <h3>4. EXISTS и NOT EXISTS</h3>
        <pre><code>
    -- Пользователи, у которых есть заказы
    SELECT * FROM users u
    WHERE EXISTS (
        SELECT 1 FROM orders o
        WHERE o.user_id = u.id
    );

    -- Пользователи без заказов
    SELECT * FROM users u
    WHERE NOT EXISTS (
        SELECT 1 FROM orders o
        WHERE o.user_id = u.id
    );
        </code></pre>

        <h3>5. ANY и ALL</h3>
        <pre><code>
    -- Пользователи старше любого пользователя из Москвы
    SELECT * FROM users
    WHERE age > ANY (
        SELECT age FROM users WHERE city = "Moscow"
    );

    -- Пользователи старше всех пользователей из Москвы
    SELECT * FROM users
    WHERE age > ALL (
        SELECT age FROM users WHERE city = "Moscow"
    );
        </code></pre>

        <h2>Оконные функции (Window Functions):</h2>

        <pre><code>
    -- ROW_NUMBER — нумерация строк
    SELECT
        username,
        salary,
        ROW_NUMBER() OVER (ORDER BY salary DESC) AS rank
    FROM employees;

    -- RANK — ранжирование с пропусками
    SELECT
        username,
        salary,
        RANK() OVER (ORDER BY salary DESC) AS rank
    FROM employees;

    -- DENSE_RANK — ранжирование без пропусков
    SELECT
        username,
        salary,
        DENSE_RANK() OVER (ORDER BY salary DESC) AS rank
    FROM employees;

    -- PARTITION BY — группировка внутри оконной функции
    SELECT
        username,
        department,
        salary,
        RANK() OVER (PARTITION BY department ORDER BY salary DESC) AS dept_rank
    FROM employees;

    -- LAG/LEAD — доступ к предыдущей/следующей строке
    SELECT
        username,
        salary,
        LAG(salary) OVER (ORDER BY salary) AS prev_salary,
        LEAD(salary) OVER (ORDER BY salary) AS next_salary
    FROM employees;
        </code></pre>

        <h2>Практические примеры:</h2>

        <h3>Ранжирование продаж:</h3>
        <pre><code>
    -- Топ-3 продукта в каждой категории по продажам
    SELECT
        category,
        product_name,
        sales,
        RANK() OVER (PARTITION BY category ORDER BY sales DESC) AS rank_in_category
    FROM products
    WHERE rank_in_category <= 3;
        </code></pre>

        <h3>Расчет накопительного итога:</h3>
        <pre><code>
    -- Кумулятивная сумма продаж по месяцам
    SELECT
        DATE_FORMAT(created_at, "%Y-%m") AS month,
        SUM(total) AS monthly_sales,
        SUM(SUM(total)) OVER (ORDER BY DATE_FORMAT(created_at, "%Y-%m")) AS cumulative_sales
    FROM orders
    WHERE status = "completed"
    GROUP BY DATE_FORMAT(created_at, "%Y-%m");
        </code></pre>

        <h2>Лучшие практики:</h2>
        <ul>
            <li>✅ Используйте индексы для ускорения JOIN</li>
            <li>✅ Избегайте SELECT * (указывайте нужные колонки)</li>
            <li>✅ Используйте LIMIT для ограничения результатов</li>
            <li>✅ Применяйте EXPLAIN для анализа запросов</li>
            <li>✅ Кешируйте частые запросы</li>
            <li>✅ Используйте EXISTS вместо IN для больших таблиц</li>
        </ul>',
        1
    );

INSERT INTO courses (id, course_type, description, author_id,test_id)
VALUES (
           3,
           'COLLECTIONS',
           'Краткий обзор базовых коллекций Java: List, Set, Map, Queue и их основные реализации',
           5,
        2
       );

INSERT INTO theory_materials (id, content, course_id)
VALUES
    (
        11,
        '<h1>Введение в коллекции Java</h1>

        <p><strong>Коллекции (Collections)</strong> — это фреймворк в Java, который предоставляет структуры данных для хранения и обработки групп объектов.</p>

        <h2>Что такое коллекции?</h2>
        <p>Коллекции — это контейнеры, которые позволяют хранить группы объектов и предоставляют методы для их обработки.</p>

        <h2>Иерархия коллекций:</h2>
        <ul>
            <li><strong>Collection</strong> — корневой интерфейс</li>
            <li><strong>List</strong> — упорядоченные коллекции с дубликатами</li>
            <li><strong>Set</strong> — уникальные элементы (без дубликатов)</li>
            <li><strong>Queue</strong> — очереди (FIFO)</li>
            <li><strong>Map</strong> — пары ключ-значение</li>
        </ul>

        <h2>Основные интерфейсы:</h2>
        <ul>
            <li><strong>List</strong> — ArrayList, LinkedList</li>
            <li><strong>Set</strong> — HashSet, TreeSet, LinkedHashSet</li>
            <li><strong>Map</strong> — HashMap, TreeMap, LinkedHashMap</li>
            <li><strong>Queue</strong> — PriorityQueue, LinkedList</li>
        </ul>

        <p>Каждая коллекция имеет свои особенности и используется в разных ситуациях.</p>',
        3
    ),
    (
        12,
        '<h1>List — упорядоченные коллекции</h1>

        <p><strong>List</strong> — это интерфейс, который представляет упорядоченную коллекцию элементов, допускающую дубликаты.</p>

        <h2>Основные реализации:</h2>

        <h3>1. ArrayList</h3>
        <ul>
            <li>✅ Быстрый доступ по индексу (O(1))</li>
            <li>✅ Быстрая вставка в конец (O(1))</li>
            <li>❌ Медленная вставка/удаление в середине (O(n))</li>
            <li>❌ Требует больше памяти</li>
        </ul>

        <pre><code>
    import java.util.ArrayList;
    import java.util.List;

    public class ArrayListExample {
        public static void main(String[] args) {
            // Создание ArrayList
            List<String> languages = new ArrayList<>();

            // Добавление элементов
            languages.add("Java");
            languages.add("Python");
            languages.add("Java"); // Дубликат разрешен

            // Получение элемента по индексу
            String first = languages.get(0); // "Java"

            // Размер списка
            int size = languages.size(); // 3

            // Проверка наличия элемента
            boolean hasJava = languages.contains("Java"); // true

            // Удаление элемента
            languages.remove("Python");

            // Итерация по списку
            for (String lang : languages) {
                System.out.println(lang);
            }

            // Вставка в середину
            languages.add(1, "C++");

            // Очистка списка
            languages.clear();
        }
    }
        </code></pre>

        <h3>2. LinkedList</h3>
        <ul>
            <li>✅ Быстрая вставка/удаление в середине (O(1))</li>
            <li>✅ Может работать как очередь</li>
            <li>❌ Медленный доступ по индексу (O(n))</li>
            <li>❌ Больше памяти</li>
        </ul>

        <pre><code>
    import java.util.LinkedList;
    import java.util.List;

    public class LinkedListExample {
        public static void main(String[] args) {
            // Создание LinkedList
            LinkedList<String> list = new LinkedList<>();

            // Добавление в начало
            list.addFirst("First");

            // Добавление в конец
            list.addLast("Last");

            // Добавление в середину
            list.add(1, "Middle");

            // Получение первого и последнего
            String first = list.getFirst();
            String last = list.getLast();

            // Удаление
            list.removeFirst();
            list.removeLast();

            // Использование как очереди
            list.offer("Element"); // Добавление в конец
            String element = list.poll(); // Извлечение из начала

            // Итерация
            for (String item : list) {
                System.out.println(item);
            }
        }
    }
        </code></pre>

        <h3>Когда использовать:</h3>
        <ul>
            <li><strong>ArrayList</strong> — когда нужен быстрый доступ по индексу</li>
            <li><strong>LinkedList</strong> — когда часто вставляем/удаляем в середине</li>
        </ul>',
        3
    ),
    (
        13,
        '<h1>Set — коллекции без дубликатов</h1>

        <p><strong>Set</strong> — это интерфейс, который представляет коллекцию уникальных элементов (без дубликатов).</p>

        <h2>Основные реализации:</h2>

        <h3>1. HashSet</h3>
        <ul>
            <li>✅ Быстрый доступ (O(1))</li>
            <li>✅ Неупорядоченный</li>
            <li>✅ Позволяет NULL</li>
        </ul>

        <pre><code>
    import java.util.HashSet;
    import java.util.Set;

    public class HashSetExample {
        public static void main(String[] args) {
            // Создание HashSet
            Set<String> set = new HashSet<>();

            // Добавление элементов
            set.add("Java");
            set.add("Python");
            set.add("Java"); // Не добавится (дубликат)
            set.add(null);   // Можно добавить null

            // Размер
            int size = set.size(); // 3 (Java, Python, null)

            // Проверка наличия
            boolean hasJava = set.contains("Java"); // true

            // Удаление
            set.remove("Python");

            // Итерация (порядок не гарантируется)
            for (String lang : set) {
                System.out.println(lang);
            }

            // Проверка на пустоту
            boolean empty = set.isEmpty();
        }
    }
        </code></pre>

        <h3>2. TreeSet</h3>
        <ul>
            <li>✅ Сортированный порядок</li>
            <li>❌ Медленнее HashSet (O(log n))</li>
            <li>❌ Не допускает null</li>
        </ul>

        <pre><code>
    import java.util.Set;
    import java.util.TreeSet;

    public class TreeSetExample {
        public static void main(String[] args) {
            // Создание TreeSet
            Set<Integer> numbers = new TreeSet<>();

            // Добавление
            numbers.add(5);
            numbers.add(2);
            numbers.add(8);
            numbers.add(2); // Не добавится

            // Автоматическая сортировка
            for (int num : numbers) {
                System.out.print(num + " "); // 2, 5, 8
            }

            // Работа с объектами
            Set<String> names = new TreeSet<>();
            names.add("Ivan");
            names.add("Alex");
            names.add("Maria");
            // Alex, Ivan, Maria (алфавитный порядок)

            // Получение первого и последнего
            String first = ((TreeSet<String>) names).first(); // "Alex"
            String last = ((TreeSet<String>) names).last();   // "Maria"
        }
    }
        </code></pre>

        <h3>3. LinkedHashSet</h3>
        <ul>
            <li>✅ Сохраняет порядок добавления</li>
            <li>✅ Быстрый доступ</li>
        </ul>

        <pre><code>
    import java.util.LinkedHashSet;
    import java.util.Set;

    public class LinkedHashSetExample {
        public static void main(String[] args) {
            // Создание LinkedHashSet
            Set<String> set = new LinkedHashSet<>();

            set.add("Java");
            set.add("Python");
            set.add("C++");

            // Сохраняет порядок добавления
            for (String lang : set) {
                System.out.println(lang); // Java, Python, C++
            }
        }
    }
        </code></pre>

        <h3>Когда использовать:</h3>
        <ul>
            <li><strong>HashSet</strong> — когда нужна скорость и не важен порядок</li>
            <li><strong>TreeSet</strong> — когда нужна сортировка</li>
            <li><strong>LinkedHashSet</strong> — когда важен порядок добавления</li>
        </ul>',
        3
    ),
    (
        14,
        '<h1>Map — пары ключ-значение</h1>

        <p><strong>Map</strong> — это интерфейс, который хранит пары "ключ-значение" и не допускает дублирование ключей.</p>

        <h2>Основные реализации:</h2>

        <h3>1. HashMap</h3>
        <ul>
            <li>✅ Быстрый доступ (O(1))</li>
            <li>✅ Неупорядоченный</li>
            <li>✅ Позволяет один null-ключ и множество null-значений</li>
        </ul>

        <pre><code>
    import java.util.HashMap;
    import java.util.Map;

    public class HashMapExample {
        public static void main(String[] args) {
            // Создание HashMap
            Map<String, Integer> programmingLanguages = new HashMap<>();

            // Добавление элементов
            programmingLanguages.put("Java", 1995);
            programmingLanguages.put("Python", 1991);
            programmingLanguages.put("Java", 1995); // Перезапишет, если ключ существует

            // Получение значения по ключу
            Integer year = programmingLanguages.get("Java"); // 1995

            // Получение с дефолтным значением
            Integer yearC = programmingLanguages.getOrDefault("C", 1972);

            // Проверка наличия
            boolean hasKey = programmingLanguages.containsKey("Java"); // true
            boolean hasValue = programmingLanguages.containsValue(1995); // true

            // Удаление
            programmingLanguages.remove("Python");

            // Итерация по ключам
            for (String key : programmingLanguages.keySet()) {
                System.out.println(key);
            }

            // Итерация по значениям
            for (Integer value : programmingLanguages.values()) {
                System.out.println(value);
            }

            // Итерация по парам
            for (Map.Entry<String, Integer> entry : programmingLanguages.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }

            // Замена значения
            programmingLanguages.replace("Java", 1996);

            // Размер
            int size = programmingLanguages.size();
        }
    }
        </code></pre>

        <h3>2. TreeMap</h3>
        <ul>
            <li>✅ Сортировка по ключам</li>
            <li>❌ Медленнее (O(log n))</li>
        </ul>

        <pre><code>
    import java.util.Map;
    import java.util.TreeMap;

    public class TreeMapExample {
        public static void main(String[] args) {
            // Создание TreeMap
            Map<String, Integer> languages = new TreeMap<>();

            languages.put("Java", 1995);
            languages.put("Python", 1991);
            languages.put("C++", 1985);

            // Автоматическая сортировка по ключам
            for (Map.Entry<String, Integer> entry : languages.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            // C++: 1985, Java: 1995, Python: 1991

            // Получение первого и последнего ключа
            String firstKey = ((TreeMap<String, Integer>) languages).firstKey(); // "C++"
            String lastKey = ((TreeMap<String, Integer>) languages).lastKey();   // "Python"
        }
    }
        </code></pre>

        <h3>3. LinkedHashMap</h3>
        <ul>
            <li>✅ Сохраняет порядок добавления</li>
            <li>✅ Быстрый доступ</li>
        </ul>

        <pre><code>
    import java.util.LinkedHashMap;
    import java.util.Map;

    public class LinkedHashMapExample {
        public static void main(String[] args) {
            // Создание LinkedHashMap
            Map<String, Integer> languages = new LinkedHashMap<>();

            languages.put("Java", 1995);
            languages.put("Python", 1991);
            languages.put("C++", 1985);

            // Сохраняет порядок добавления
            for (Map.Entry<String, Integer> entry : languages.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            // Java: 1995, Python: 1991, C++: 1985
        }
    }
        </code></pre>',
        3
    ),
    (
        15,
        '<h1>Queue и другие коллекции</h1>

        <p><strong>Queue</strong> — это интерфейс для коллекций, работающих по принципу FIFO (первый пришел - первый ушел).</p>

        <h2>Queue (Очередь)</h2>

        <h3>Основные методы:</h3>
        <ul>
            <li><strong>offer()</strong> — добавить элемент (возвращает false при ошибке)</li>
            <li><strong>add()</strong> — добавить элемент (бросает исключение)</li>
            <li><strong>poll()</strong> — извлечь и удалить первый элемент (возвращает null)</li>
            <li><strong>remove()</strong> — извлечь и удалить первый элемент (бросает исключение)</li>
            <li><strong>peek()</strong> — посмотреть первый элемент (возвращает null)</li>
            <li><strong>element()</strong> — посмотреть первый элемент (бросает исключение)</li>
        </ul>

        <h3>Реализации:</h3>

        <pre><code>
    import java.util.LinkedList;
    import java.util.PriorityQueue;
    import java.util.Queue;

    public class QueueExample {
        public static void main(String[] args) {
            // PriorityQueue — очередь с приоритетом
            Queue<Integer> priorityQueue = new PriorityQueue<>();
            priorityQueue.offer(5);
            priorityQueue.offer(1);
            priorityQueue.offer(3);
            System.out.println(priorityQueue.poll()); // 1 (наименьший)
            System.out.println(priorityQueue.poll()); // 3
            System.out.println(priorityQueue.poll()); // 5

            // LinkedList как очередь
            Queue<String> queue = new LinkedList<>();
            queue.offer("Первый");
            queue.offer("Второй");
            queue.offer("Третий");

            String first = queue.poll(); // "Первый"
            String next = queue.peek();  // "Второй"
            String third = queue.poll(); // "Второй"

            // Проверка на пустоту
            boolean empty = queue.isEmpty();
        }
    }
        </code></pre>

        <h2>Deque (Двусторонняя очередь)</h2>

        <pre><code>
    import java.util.ArrayDeque;
    import java.util.Deque;
    import java.util.LinkedList;

    public class DequeExample {
        public static void main(String[] args) {
            // LinkedList как Deque
            Deque<String> deque = new LinkedList<>();

            // Добавление в начало и конец
            deque.addFirst("Первый");
            deque.addLast("Последний");
            deque.offerFirst("Новый первый");
            deque.offerLast("Новый последний");

            // Извлечение
            String first = deque.pollFirst();
            String last = deque.pollLast();

            // Просмотр
            String peekFirst = deque.peekFirst();
            String peekLast = deque.peekLast();

            // ArrayDeque — более эффективная реализация
            Deque<Integer> arrayDeque = new ArrayDeque<>();
            arrayDeque.push(1);  // Добавление в начало
            arrayDeque.push(2);
            arrayDeque.push(3);

            int top = arrayDeque.pop(); // 3 (LIFO)
        }
    }
        </code></pre>

        <h2>Сравнение основных коллекций:</h2>

        <table border="1" style="border-collapse: collapse; width: 100%;">
            <tr>
                <th>Коллекция</th>
                <th>Упорядоченность</th>
                <th>Дубликаты</th>
                <th>Скорость</th>
            </tr>
            <tr>
                <td>ArrayList</td>
                <td>По индексу</td>
                <td>Да</td>
                <td>Быстрый доступ</td>
            </tr>
            <tr>
                <td>LinkedList</td>
                <td>По индексу</td>
                <td>Да</td>
                <td>Быстрая вставка</td>
            </tr>
            <tr>
                <td>HashSet</td>
                <td>Нет</td>
                <td>Нет</td>
                <td>Быстрый</td>
            </tr>
            <tr>
                <td>TreeSet</td>
                <td>Сортированный</td>
                <td>Нет</td>
                <td>Средний</td>
            </tr>
            <tr>
                <td>HashMap</td>
                <td>Нет</td>
                <td>Нет (ключи)</td>
                <td>Быстрый</td>
            </tr>
            <tr>
                <td>TreeMap</td>
                <td>Сортированный</td>
                <td>Нет (ключи)</td>
                <td>Средний</td>
            </tr>
        </table>

        <h2>Выбор коллекции:</h2>
        <ul>
            <li><strong>List</strong> — нужен порядок и дубликаты</li>
            <li><strong>Set</strong> — нужны уникальные элементы</li>
            <li><strong>Map</strong> — нужны пары ключ-значение</li>
            <li><strong>Queue</strong> — нужна обработка в порядке поступления</li>
        </ul>',
        3
    );

SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses) + 1);
SELECT setval('theory_materials_id_seq', (SELECT MAX(id) FROM theory_materials) + 1);