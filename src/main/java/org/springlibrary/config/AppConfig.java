package org.springlibrary.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springlibrary.repositories.BooksRepository;


@Configuration
@ComponentScan(basePackages = "org.springlibrary")
public class AppConfig {
    
}
