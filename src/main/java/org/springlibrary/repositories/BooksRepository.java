package org.springlibrary.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springlibrary.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BooksRepository {
    private static final String COLUMN_ID = "Book_ID";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_AUTHOR = "Author";
    private static final String COLUMN_DESCRIPTION = "Description";

    private final JdbcTemplate template;

    public BooksRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    public Optional<Book> findById(long id) {
        String sql = "SELECT * FROM Book WHERE Book_ID = ?";

        try {
            return Optional.ofNullable(template.queryForObject(sql, (rs, _) -> new Book(
                    rs.getLong(COLUMN_ID),
                    rs.getString(COLUMN_TITLE),
                    rs.getString(COLUMN_AUTHOR),
                    rs.getString(COLUMN_DESCRIPTION)
            ), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Book> findByTitle(String title) {
        String sql = "SELECT * FROM Book WHERE Title = ?";

        try {
            return Optional.ofNullable(template.queryForObject(sql, (rs, _) -> new Book(
                    rs.getLong(COLUMN_ID),
                    rs.getString(COLUMN_TITLE),
                    rs.getString(COLUMN_AUTHOR),
                    rs.getString(COLUMN_DESCRIPTION)
            ), title));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM Book";

        return template.query(sql, (rs, _) -> new Book(
                rs.getLong(COLUMN_ID),
                rs.getString(COLUMN_TITLE),
                rs.getString(COLUMN_AUTHOR),
                rs.getString(COLUMN_DESCRIPTION)
        ));
    }

    public int create(Book book) {
        String sql = "INSERT INTO Book (Title, Author, Description) VALUES (?, ?, ?)";

        return template.update(sql, book.getTitle(), book.getAuthor(), book.getDescription());
    }

    public int update(Book book) {
        String sql = "UPDATE Book Set Title = ?, Author = ?, Description = ? WHERE Book_ID = ?";

        return template.update(sql, book.getTitle(), book.getAuthor(), book.getDescription(), book.getId());
    }

    public int deleteById(long id) {
        String sql = "DELETE FROM Book WHERE Book_ID = ?";

        return template.update(sql, id);
    }

    public int deleteByTitle(String title) {
        String sql = "DELETE FROM Book WHERE Title = ?";

        return template.update(sql, title);
    }
}
