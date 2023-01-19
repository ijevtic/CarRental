INSERT INTO state (id, name) VALUES (1, 'OK'), (2, 'NOT_VERIFIED'), (3, 'BAN'), (4, 'BAN_AND_NOT_VERIFIED');

INSERT INTO role (id, description, name) VALUES (1, 'This is admin', 'ADMIN'), (2, 'This is user', 'USER'), (3, 'This is manager', 'MANAGER');

INSERT INTO user (id, email, username, password, state_id, role_id, total_rent_days) VALUES (2, 'admin@admin.com', 'admin', '12345678', 1, 1, 0)