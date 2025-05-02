INSERT INTO users (username, email, password) VALUES
('admin', 'admin@example.com', 'hashed-password-123'),
('user1', 'user1@example.com', 'hashed-password-456');

INSERT INTO roles (name) VALUES
('ADMIN'),
('USER');

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),
(2, 2);
