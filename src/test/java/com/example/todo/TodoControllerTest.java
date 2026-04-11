package com.example.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAndGetTodo() throws Exception {
        // Create a todo
        String todoJson = "{\"title\": \"Test Task\", \"completed\": false}";
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJson))
                .andExpect(status().isCreated());

        // Get the created todo
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testDeleteTodo() throws Exception {
        // Create a todo first
        String todoJson = "{\"title\": \"To Delete\", \"completed\": false}";
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJson))
                .andExpect(status().isCreated());

        // Delete the todo
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetNonExistentTodo() throws Exception {
        // Try to get a todo that doesn't exist
        mockMvc.perform(get("/api/todos/999"))
                .andExpect(status().isNotFound());
    }
}
