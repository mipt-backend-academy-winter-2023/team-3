CREATE TABLE "user"
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL
);