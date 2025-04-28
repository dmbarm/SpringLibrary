package org.springlibrary.services;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessagesService {
    private final MessageSource messageSource;
    private final InputService inputService;

    private Locale locale;

    public MessagesService(MessageSource messageSource, InputService inputService) {
        this.messageSource = messageSource;
        this.inputService = inputService;
    }

    public void selectLanguage() {
        String language = inputService.prompt("Select language (en/pl/ru)");
        this.locale = switch (language) {
            case "pl" -> Locale.of("pl");
            case "ru" -> Locale.of("ru");
            case "en" -> Locale.of("en");
            default -> {
                System.out.println("Invalid language. Defaulting to English.");
                yield Locale.ENGLISH;
            }
        };
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, locale);
    }
}