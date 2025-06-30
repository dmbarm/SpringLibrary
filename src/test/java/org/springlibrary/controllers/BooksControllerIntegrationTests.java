package org.springlibrary.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springlibrary.config.TestContainersConfig;
import org.springlibrary.repositories.BooksRepository;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestContainersConfig.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class BooksControllerIntegrationTests extends IntegrationTestsBase {
    private static final String BOOK_NAME = "TestBook";
    private static final String BOOK_AUTHOR = "TestAuthor";
    private static final String BOOK_DESCRIPTION = "TestDescription";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BooksRepository booksRepository;

    @BeforeEach
    void clearDatabase() {
        booksRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void shouldReturnAllBooks() {
        int index = 1;
        createTestBook(BOOK_NAME + index, BOOK_AUTHOR + index, BOOK_DESCRIPTION + index++);
        createTestBook(BOOK_NAME + index, BOOK_AUTHOR + index, BOOK_DESCRIPTION + index++);
        createTestBook(BOOK_NAME + index, BOOK_AUTHOR + index, BOOK_DESCRIPTION + index);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
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
                        .contentType(MediaType.APPLICATION_JSON)
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
                        }
                """, bookId);

        mockMvc.perform(patch("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        return Long.parseLong(result.getResponse().getContentAsString());
    }
}
