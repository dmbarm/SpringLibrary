package org.springlibrary;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springlibrary.config.AppConfig;
import org.springlibrary.controllers.MainController;

public class SpringLibraryApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MainController mainController = appContext.getBean(MainController.class);
        mainController.startUserInput();
    }
}