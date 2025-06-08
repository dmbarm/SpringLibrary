package org.springlibrary.controllers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springlibrary.services.MessagesService;

@Component
public class MainController implements CommandLineRunner {
    private final MessagesService messagesService;
    private final LibraryController libraryController;


    public MainController(MessagesService messagesService, LibraryController libraryController) {
        this.messagesService = messagesService;
        this.libraryController = libraryController;
    }

    @Override
    public void run(String... args) {
        if (System.console() == null) {
            return;
        }
        messagesService.selectLanguage();
        libraryController.startBookManagement();
    }
}
