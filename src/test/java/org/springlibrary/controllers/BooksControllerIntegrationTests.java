package org.springlibrary.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springlibrary.config.TestContainersConfig;
import org.springlibrary.entities.Book;
import org.springlibrary.repositories.BooksRepository;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestContainersConfig.class)
@WithMockUser(username = "admin", roles = {"ROLE_ADMIN"})
class BooksControllerIntegrationTests {
    private static final String BOOK_NAME = "TestBook";
    private static final String BOOK_AUTHOR = "TestAuthor";
    private static final String BOOK_DESCRIPTION = "TestBook";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BooksRepository booksRepository;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestContainersConfig.POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", TestContainersConfig.POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", TestContainersConfig.POSTGRES_CONTAINER::getPassword);
    }

    @BeforeEach
    void setupDatabase() {
        booksRepository.deleteAll();
        booksRepository.save(new Book("Book", "Author", "Description"));
    }

    @SneakyThrows
    @Test
    void shouldReturnAllBooks() {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("TestBook"));
    }

    @SneakyThrows
    @Test
    void shouldReturnBookById() {
        long bookId = createTestBook(BOOK_NAME, BOOK_AUTHOR, BOOK_DESCRIPTION);

        mockMvc.perform(get(String.format("/api/books/id/%d", bookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestBook"));
    }

    @SneakyThrows
    @Test
    void shouldReturnBookByTitle() {
        createTestBook(BOOK_NAME, BOOK_AUTHOR, BOOK_DESCRIPTION);

        mockMvc.perform(get(String.format("/api/books/title/%s", BOOK_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("TestAuthor"));
    }

    @SneakyThrows
    @Test
    void shouldAddBookSuccessfully() {
        String jsonBook = """
                {
                    "title": "TestBook",
                    "author": "TestAuthor",
                    "description": "TestDescription"
                }""";

        mockMvc.perform(post("/api/books")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBook))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/books/")))
                .andExpect(jsonPath("$").isNumber());
    }

    @SneakyThrows
    @Test
    void shouldUpdateBookSuccessfully() {
        long bookId = createTestBook(BOOK_NAME, BOOK_AUTHOR, BOOK_DESCRIPTION);

        mockMvc.perform(get(String.format("/api/books/id/%d", bookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestBook"))
                .andExpect(jsonPath("$.author").value("TestAuthor"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        String jsonBook = String.format("""
                        {
                            "id": %d,
                            "title": "ChangedTestBook",
                            "author": "ChangedTestAuthor",
                            "description": "ChangedTestDescription"
                """, bookId);

        mockMvc.perform(patch("/api/books")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBook))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(String.format("/api/books/id/%d", bookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("ChangedTestBook"))
                .andExpect(jsonPath("$.author").value("ChangedTestAuthor"))
                .andExpect(jsonPath("$.description").value("ChangedTestDescription"));
    }

    @SneakyThrows
    @Test
    void shouldDeleteBookById() {
        long bookId = createTestBook(BOOK_NAME, BOOK_AUTHOR, BOOK_DESCRIPTION);

        mockMvc.perform(get(String.format("/api/books/id/%d", bookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestBook"));

        mockMvc.perform(delete(String.format("/api/books/id/%d", bookId)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(String.format("/api/books/id/%d", bookId)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldDeleteBookByTitle() {
        createTestBook(BOOK_NAME, BOOK_AUTHOR, BOOK_DESCRIPTION);

        mockMvc.perform(get(String.format("/api/books/title/%s", BOOK_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestBook"));

        mockMvc.perform(delete(String.format("/api/books/title/%s", BOOK_NAME)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(String.format("/api/books/title/%s", BOOK_NAME)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    private long createTestBook(String title, String author, String description) {
        String json = String.format("""
                {
                    "title": "%s",
                    "author": "%s",
                    "description": "%s"
                }
                """, title, author, description);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        return Long.parseLong(result.getResponse().getContentAsString());
    }
}
