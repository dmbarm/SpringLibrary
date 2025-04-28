package org.springlibrary.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springlibrary.models.Book;

import java.util.List;

@Repository
public class BooksRepository {

    private final JdbcTemplate template;

    public BooksRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM Book";

        return template.query(sql, (rs, _) -> new Book(
                rs.getLong("Book_ID"),
                rs.getString("Title"),
                rs.getString("Author"),
                rs.getString("Description")
        ));
    }

    public void saveAll(List<Book> books) {
        String sql = "UPDATE Book SET Title = ?, Author = ?, Description = ? WHERE Book_ID = ?";

        template.batchUpdate(sql, books, books.size(),
                (ps, book) -> {
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    ps.setString(3, book.getDescription());
                    ps.setLong(4, book.getId());
                });
    }
}
