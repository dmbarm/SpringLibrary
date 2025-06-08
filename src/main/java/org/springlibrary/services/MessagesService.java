package org.springlibrary.services;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springlibrary.services.io.IOService;

import java.util.Locale;

@Service
public class MessagesService {
    private final MessageSource messageSource;
    private final IOService ioService;

    public MessagesService(MessageSource messageSource, IOService ioService) {
        this.messageSource = messageSource;
        this.ioService = ioService;
    }

    public void selectLanguage() {
        Locale locale;
        String language = ioService.prompt("Select language (en/pl/ru)");
        locale = switch (language) {
            case "pl" -> Locale.of("pl");
            case "ru" -> Locale.of("ru");
            case "en" -> Locale.of("en");
            default -> {
                ioService.print("Invalid language. Defaulting to English.");
                yield Locale.ENGLISH;
            }
        };

        LocaleContextHolder.setLocale(locale);
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}