package org.springlibrary.config;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springlibrary.repositories.BooksRepository;

@Configuration
@EnableTransactionManagement
public class TestAppConfig {

    @Bean
    public BooksRepository booksRepository(SessionFactory sessionFactory) {
        return new org.springlibrary.repositories.BooksRepository(sessionFactory);
    }

    @Bean
    public Statistics hibernateStatistics(SessionFactory sessionFactory) {
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        return stats;
    }
}