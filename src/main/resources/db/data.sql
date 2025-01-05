-- Insert Initial User
INSERT INTO `user` (username, password, email) VALUES ('testuser', 'password', 'testuser@example.com');

-- Insert Initial Wallet Balance
INSERT INTO wallet (user_id, currency, balance) VALUES (1, 'USDT', 50000.00);