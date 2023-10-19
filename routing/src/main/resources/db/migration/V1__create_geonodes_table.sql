CREATE TABLE "geonode"
(
    id        SERIAL PRIMARY KEY,
    node_type VARCHAR NOT NULL,
    name      VARCHAR,
    latitude  FLOAT   NOT NULL,
    longitude FLOAT   NOT NULL
);

