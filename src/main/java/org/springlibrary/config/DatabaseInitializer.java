package org.springlibrary.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springlibrary.exceptions.DatabaseInitializationException;
import org.springlibrary.models.Book;

import java.io.IOException;
import java.util.List;


@Component
@PropertySource("classpath:application.properties")
public class DatabaseInitializer {

    private final SessionFactory sessionFactory;

    @Value("${db.fill}")
    private String fillUrl;

    public DatabaseInitializer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    @Transactional
    public void init() {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery("SELECT COUNT(b) FROM Book b", Long.class).getSingleResult();
            if (count == 0) {
                List<Book> books = readBooksFromFile(fillUrl);
                Transaction tx = session.beginTransaction();
                for (Book b : books) {
                    session.persist(b);
                }
                tx.commit();
            }
        } catch (IOException e) {
            throw new DatabaseInitializationException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    private List<Book> readBooksFromFile(String fileName) throws IOException {
        // TODO: parse books
        throw new UnsupportedOperationException("TODO: implement parsing logic");
    }
}