package org.springlibrary.controllers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springlibrary.config.TestContainersConfig;

public abstract class IntegrationTestBase {

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestContainersConfig.POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", TestContainersConfig.POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", TestContainersConfig.POSTGRES_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
}
