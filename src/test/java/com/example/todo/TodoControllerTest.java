package com.example.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndGetTodo() throws Exception {
        Todo todo = new Todo(null, "Test Todo", false);
        // Create
        String response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsString();
        Todo created = objectMapper.readValue(response, Todo.class);
        // Get
        mockMvc.perform(get("/api/todos/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        Todo todo = new Todo(null, "Delete Me", false);
        String response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Todo created = objectMapper.readValue(response, Todo.class);
        // Delete
        mockMvc.perform(delete("/api/todos/" + created.getId()))
                .andExpect(status().isNoContent());
        // Verify not found
        mockMvc.perform(get("/api/todos/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetNonExistentTodo() throws Exception {
        mockMvc.perform(get("/api/todos/99999"))
                .andExpect(status().isNotFound());
    }
}
