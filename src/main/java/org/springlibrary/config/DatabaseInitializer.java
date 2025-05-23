package org.springlibrary.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springlibrary.exceptions.DatabaseInitializationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


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
        Session session = sessionFactory.getCurrentSession();
        try {
            long count = (session
                    .createNativeQuery("SELECT COUNT(*) FROM Book", Number.class)
                    .getSingleResult())
                    .longValue();

            if (count == 0) {
                session.createNativeQuery(readSqlFile(fillUrl), Void.class).executeUpdate();
            }
        } catch (IOException e) {
            throw new DatabaseInitializationException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    private String readSqlFile(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName.replace("classpath:", ""));
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}