package org.springlibrary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class AppConfig {
    @Value("${spring.messages.basename:messages}")
    private String basename;

    @Value("${spring.messages.default-locale:en}")
    private String defaultLocale;

    @Value("${spring.messages.encoding:UTF-8}")
    private String encoding;

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(basename);
        source.setDefaultLocale(Locale.forLanguageTag(defaultLocale));
        source.setDefaultEncoding(encoding);

        return source;
    }
}
