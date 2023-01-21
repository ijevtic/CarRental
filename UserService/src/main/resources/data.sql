INSERT INTO state (id, name) VALUES (1, 'OK'), (2, 'NOT_VERIFIED'), (3, 'BAN'), (4, 'BAN_AND_NOT_VERIFIED');

INSERT INTO role (id, description, name) VALUES (1, 'This is admin', 'ADMIN'), (2, 'This is user', 'USER'), (3, 'This is manager', 'MANAGER');

INSERT INTO user (id, email, username, password, state_id, role_id, total_rent_days) VALUES (2, 'admin@admin.com', 'admin', '12345678', 1, 1, 0);

INSERT INTO user (id, email, username, password, state_id, role_id, total_rent_days) VALUES (8, 'ivan8@gmail.com', 'ivan8', '12345678', 1, 2, 0);

INSERT INTO user_rank (id, rank_name, min_days, max_days, discount_amount) VALUES (1, "BRONZE", 0, 5, 0.1),
(2, "SILVER", 6, 10, 0.2), (3, "GOLD", 11, 15, 0.3), (4, "PLATINUM", 16, 20, 0.4), (5, "DIAMOND", 21, 25, 0.5);