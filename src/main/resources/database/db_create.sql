CREATE TABLE IF NOT EXISTS Book
(
    Book_ID     serial       NOT NULL,
    Title       varchar(255) NOT NULL,
    Author      varchar(255) NOT NULL,
    Description text         NOT NULL,
    CONSTRAINT Book_pk PRIMARY KEY (Book_ID)
);
