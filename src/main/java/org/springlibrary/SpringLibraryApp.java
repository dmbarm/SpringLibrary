package org.springlibrary;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springlibrary.config.AppConfig;
import org.springlibrary.controllers.LibraryController;

public class SpringLibraryApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        LibraryController libraryController = appContext.getBean(LibraryController.class);
        libraryController.startUserInput();

    }
}