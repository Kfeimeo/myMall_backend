USE  mymall;
DROP TABLE customer;
CREATE TABLE User (
                      username VARCHAR(255) PRIMARY KEY,
                      password VARCHAR(255) NOT NULL
);
INSERT INTO customer (username, password) VALUES ('testuser', 'password123');
INSERT INTO customer (username, password) VALUES ('admin', 'admin');
