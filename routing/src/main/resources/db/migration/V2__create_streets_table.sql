CREATE TABLE "street"
(
    id       SERIAL PRIMARY KEY,
    name VARCHAR,
    from_latitude INT NOT NULL,
    from_longitude INT NOT NULL,
    to_latitude INT NOT NULL,
    to_longitude INT NOT NULL
);

