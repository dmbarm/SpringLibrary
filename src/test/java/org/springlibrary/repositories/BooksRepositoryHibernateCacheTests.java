package org.springlibrary.repositories;

import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.support.TransactionTemplate;
import org.springlibrary.config.DatabaseConfig;
import org.springlibrary.config.TestAppConfig;
import org.springlibrary.models.Book;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringJUnitConfig(classes = {TestAppConfig.class, DatabaseConfig.class})
class BooksRepositoryHibernateCacheTests {

    @Autowired
    private Statistics stats;
    @Autowired
    private BooksRepository repo;
    @Autowired
    private HibernateTransactionManager txManager;

    private long bookId;

    @BeforeEach
    void setUp() {

        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        bookId = tmpl.execute(_ -> {
            Book b = new Book("Title", "Author", "Description");
            repo.create(b);
            return b.getId();
        });

        stats.clear();
    }

    @AfterEach
    void cleanUp() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.executeWithoutResult(_ -> repo.deleteByTitle("Title"));
    }

    @Test
    void firstLevelCache_withinSameSession() {
        new TransactionTemplate(txManager).execute(_ -> {
            repo.findById(bookId);
            repo.findById(bookId);
            return null;
        });

        assertEquals("Only one SQL prepare", 1L, stats.getPrepareStatementCount());
        assertEquals("No L2 puts yet", 1L, stats.getSecondLevelCachePutCount());
    }

    @Test
    void secondLevelCache_acrossSessions() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);

        tmpl.execute(_ -> {
            repo.findById(bookId);
            return null;
        });

        tmpl.execute(_ -> {
            repo.findById(bookId);
            return null;
        });

        assertEquals("One L2 put occurred", 1L, stats.getSecondLevelCachePutCount());
        assertEquals("One L2 hit occurred", 1L, stats.getSecondLevelCacheHitCount());
        assertEquals("Just the initial SQL", 1L, stats.getPrepareStatementCount());
    }
}
