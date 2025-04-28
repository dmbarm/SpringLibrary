package org.springlibrary.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springlibrary.exceptions.DatabaseInitializationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@PropertySource("classpath:application.properties")
public class DatabaseInitializer {

    private final JdbcTemplate template;

    @Value("${db.create}")
    private String createUrl;

    @Value("${db.fill}")
    private String fillUrl;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            String ddl = readSqlFile(createUrl);
            template.execute(ddl);

            Integer count = template.queryForObject("SELECT COUNT(*) FROM Book", Integer.class);
            if (count == null || count == 0) {
                String dml = readSqlFile(fillUrl);
                template.update(dml);
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