package org.springlibrary.services;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springlibrary.services.io.ConsoleService;
import org.springlibrary.services.io.IOService;

import java.util.Locale;

@Service
public class MessagesService {
    private final MessageSource messageSource;
    private final IOService ioService;

    private Locale locale;

    public MessagesService(MessageSource messageSource, ConsoleService consoleService) {
        this.messageSource = messageSource;
        this.ioService = consoleService;
    }

    public void selectLanguage() {
        String language = ioService.prompt("Select language (en/pl/ru)");
        this.locale = switch (language) {
            case "pl" -> Locale.of("pl");
            case "ru" -> Locale.of("ru");
            case "en" -> Locale.of("en");
            default -> {
                ioService.print("Invalid language. Defaulting to English.");
                yield Locale.ENGLISH;
            }
        };
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, locale);
    }
}