CREATE TABLE Book
(
    BookId          BIGSERIAL PRIMARY KEY,
    Title       VARCHAR(255),
    Author      VARCHAR(255),
    Description VARCHAR(2000)
);