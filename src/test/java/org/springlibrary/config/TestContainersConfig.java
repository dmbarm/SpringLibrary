package org.springlibrary.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration
public class TestContainersConfig {

    @Container
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testDb")
            .withUsername("testUser")
            .withPassword("testPassword");

    static {
        POSTGRES_CONTAINER.start();
    }

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return POSTGRES_CONTAINER;
    }
}
