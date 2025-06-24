package org.springlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLibraryApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringLibraryApp.class, args);
    }

   /* @Bean
    public CommandLineRunner commandLineRunner(GridFsTemplate gfst) {
        return _ -> {
            // ? clearing previous
            gfst.delete(new Query());

            // ? loading new
            File imagesDir = new File("/Users/dmitriy/IdeaProjects/SpringLibrary/src/main/resources/db/images");

            if (!imagesDir.exists() && !imagesDir.isDirectory())
                throw new RuntimeException("Images directory does not exist or is not a directory.");

            for (File nestedImage : imagesDir.listFiles()) {
                try (InputStream is = new FileInputStream(nestedImage)) {
                    ObjectId imageId = gfst.store(is, nestedImage.getName(), "image/jpeg");
                    System.out.println("Image " + nestedImage.getName() + " has been stored with id: " + imageId.toHexString());
                }
            }
        };
    }*/
}