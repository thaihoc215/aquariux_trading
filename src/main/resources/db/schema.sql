-- Create Users Table
CREATE TABLE `user` (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Wallets Table
CREATE TABLE wallet (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         currency VARCHAR(10) NOT NULL,
                         balance DECIMAL(20, 8) NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES `user`(id)
);

-- Create Trades Table
CREATE TABLE trade (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        trade_type VARCHAR(4) NOT NULL,
                        currency_pair VARCHAR(10) NOT NULL,
                        price DECIMAL(20, 8) NOT NULL,
                        amount DECIMAL(20, 8) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES `user`(id)
);

-- Create Prices Table
CREATE TABLE price (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        currency_pair VARCHAR(10) NOT NULL,
                        bid_price DECIMAL(20, 8) NOT NULL,
                        ask_price DECIMAL(20, 8) NOT NULL,
                        source VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Crypto Holdings Table
CREATE TABLE crypto_holding (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 wallet_id BIGINT NOT NULL,
                                 crypto_currency VARCHAR(10) NOT NULL,
                                 amount DECIMAL(20, 8) NOT NULL,
                                 FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);