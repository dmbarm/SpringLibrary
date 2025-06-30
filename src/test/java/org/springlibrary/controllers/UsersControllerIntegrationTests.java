package org.springlibrary.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
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
import org.springlibrary.entities.Role;
import org.springlibrary.repositories.RolesRepository;
import org.springlibrary.repositories.UsersRepository;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestContainersConfig.class)
class UsersControllerIntegrationTests extends IntegrationTestsBase {
    private static final String USERNAME = "TestUser";
    private static final String PASSWORD = "TestPassword";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @BeforeAll
    void setup() {
        rolesRepository.save(new Role("ADMIN"));
        rolesRepository.save(new Role("USER"));
    }

    @BeforeEach
    void clearDatabase() {
        usersRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void shouldRegisterSuccessfully() {
        String jsonUser = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, USERNAME, PASSWORD);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isString());
    }

    @SneakyThrows
    @Test
    void shouldLoginSuccessfully() {
        createTestUser(USERNAME, PASSWORD);
        String jsonUser = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, USERNAME, PASSWORD);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllUsers() {
        int index = 1;
        createTestUser(USERNAME + index, PASSWORD + index++);
        createTestUser(USERNAME + index, PASSWORD + index++);
        createTestUser(USERNAME + index, PASSWORD + index);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    //String as return type is kept in case some other methods would require jwt token for something
    @SneakyThrows
    private String createTestUser(String username, String password) {
        String json = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        return result.getResponse().getContentAsString();
    }
}
