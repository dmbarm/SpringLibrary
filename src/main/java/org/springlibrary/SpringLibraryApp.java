package org.springlibrary;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springlibrary.config.AppConfig;

public class SpringLibraryApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        SpringLibraryApp app = appContext.getBean(SpringLibraryApp.class);
        app.run();
    }

    private void run() {

    }
}