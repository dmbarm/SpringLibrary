package org.springlibrary.controllers;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springlibrary.services.MessagesService;

@Controller
public class MainController {
    private final MessagesService messagesService;
    private final LibraryController libraryController;


    public MainController(MessagesService messagesService, LibraryController libraryController) {
        this.messagesService = messagesService;
        this.libraryController = libraryController;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void startUserInput() {
        messagesService.selectLanguage();
        libraryController.startBookManagement();
    }
}
