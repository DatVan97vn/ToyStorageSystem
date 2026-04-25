Drop Database inventory;
CREATE DATABASE inventory;
USE inventory;


CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       secret VARCHAR(100),
                       using2fa BOOLEAN DEFAULT FALSE,
                       twofa_enabled BOOLEAN DEFAULT FALSE
);
