-- Додавання користувачів
INSERT INTO users (username, email, password) VALUES
('admin', 'admin@example.com', 'hashed-password-123'),
('user1', 'user1@example.com', 'hashed-password-456');

-- Додавання ролей
INSERT OR IGNORE INTO roles (name) VALUES
('ADMIN'),
('USER');

-- Призначення ролей користувачам
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),  -- admin -> ADMIN
(2, 2);  -- user1 -> USER

-- Типи SQL-запитів
INSERT OR IGNORE INTO query_types (name) VALUES
('SELECT'),
('INSERT'),
('UPDATE'),
('DELETE');

-- SQL-запити користувачів
INSERT INTO sql_queries (user_id, query_type_id, query_text, executed_at) VALUES
(1, 1, 'SELECT * FROM employees;', '2025-05-13T14:00:00'),
(2, 2, 'INSERT INTO employees (name, position) VALUES ("Іван", "менеджер");', '2025-05-13T14:05:00'),
(2, 3, 'UPDATE employees SET salary = salary * 1.1;', '2025-05-13T14:10:00'),
(1, 4, 'DELETE FROM employees WHERE contract_type = "temporary";', '2025-05-13T14:15:00');
