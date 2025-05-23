-- Ролі
INSERT INTO roles (name) VALUES
('ADMIN'),
('USER'),
('MANAGER'),
('VIEWER'),
('TESTER');

-- Типи запитів
INSERT INTO query_types (name) VALUES
('SELECT'),
('INSERT'),
('UPDATE'),
('DELETE'),
('CREATE');

-- Користувачі
INSERT INTO users (name, email, password, role_id) VALUES
('Іван Іванов', 'ivan@example.com', 'pass123', 1),
('Марія Коваль', 'maria@example.com', 'pass123', 2),
('Олег Петренко', 'oleg@example.com', 'pass123', 3),
('Світлана Дорошенко', 'svitlana@example.com', 'pass123', 4),
('Юрій Несторенко', 'yurii@example.com', 'pass123', 5);

-- SQL-запити
INSERT INTO sql_queries (user_id, query_type_id, query_text) VALUES
(1, 1, 'SELECT * FROM users;'),
(2, 2, 'INSERT INTO users (name, email, password, role_id) VALUES ("Тест", "test@mail.com", "pass", 2);'),
(3, 3, 'UPDATE users SET name = "Олег М." WHERE id = 3;'),
(4, 4, 'DELETE FROM sql_queries WHERE id = 1;'),
(5, 5, 'CREATE TABLE test_table (id INTEGER PRIMARY KEY, name TEXT);');
