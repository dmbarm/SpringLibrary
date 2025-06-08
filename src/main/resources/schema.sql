CREATE TABLE book
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    author      VARCHAR(255),
    description VARCHAR(2000)
);