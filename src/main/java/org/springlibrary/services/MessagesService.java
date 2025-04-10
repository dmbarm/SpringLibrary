package org.springlibrary.services;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class MessagesService {
    private final InputService inputService;

    private ResourceBundle resourceBundle;

    public MessagesService(InputService inputService) {
        this.inputService = inputService;
    }

    public void selectLanguage() {
        String language = inputService.prompt("Select language(en/pl)");
        Locale locale = switch (language) {
            case "pl" -> Locale.of("pl");
            case "en" -> Locale.of("en");
            default -> {
                System.out.println("Invalid language choice: " + language + ". Choosing default: en");
                yield Locale.of("en");
            }
        };

        resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    public String getString(String key) {
        return resourceBundle.getString(key);
    }
}
