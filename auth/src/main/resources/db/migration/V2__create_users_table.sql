CREATE TABLE "user"
(
    "id"         SERIAL,
    "username" VARCHAR NOT NULL,
    "password"  VARCHAR NOT NULL
);

INSERT INTO "user" (username, password)
VALUES  ('username', 'password'),
        ('123', '321');