package com.example.springbootproject_1.Controller;


import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.Model.TodoStatus;
import com.example.springbootproject_1.repo.TodoRepo;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TodoRepo todoRepository;

    @Test
    void getAllTodos() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            []
                        """));

    }

    @Test
    @DirtiesContext
    void postTodo() throws Exception {
        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "description": "test-description",
                                        "status": "OPEN"
                                    }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "description": "test-description",
                                "status": "OPEN"
                            }
                        """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void putTodo() throws Exception {
        //GIVEN
        Todo existingTodo = new Todo("1", "test-description", TodoStatus.OPEN);

        todoRepository.save(existingTodo);

        //WHEN
        mockMvc.perform(put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "description": "test-description-2",
                                        "status": "IN_PROGRESS"
                                    }
                                """))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "id": "1",
                                "description": "test-description-2",
                                "status": "IN_PROGRESS"
                            }
                        """));
    }

    @Test
    @DirtiesContext
    void getById() throws Exception {
        //GIVEN
        Todo existingTodo = new Todo("1", "test-description", TodoStatus.OPEN);
        todoRepository.save(existingTodo);

        //WHEN
        mockMvc.perform(get("/api/todo/1"))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "id": "1",
                                "description": "test-description",
                                "status": "OPEN"
                            }
                        """));

    }

    //updating with exception handling
    @Test
    @DirtiesContext
    void getByIdTest_whenInvalidId_thenStatus404() throws Exception {
        String nonExistentId = "non-existent-id";//doesn't exist in the database, to trigger exception
       // assertThrows(ServletException.class, () -> mockMvc.perform(get("/api/todo/1")));
        mockMvc.perform(get("/api/todo/" + nonExistentId))
                .andExpect(status().isNotFound()) //expected status
                .andExpect(content().string("Todo not found"));

    }

    @Test
    @DirtiesContext
    void deleteTodoById() throws Exception {
        //GIVEN
        Todo existingTodo = new Todo("1", "test-description", TodoStatus.OPEN);
        todoRepository.save(existingTodo);

        //WHEN
        mockMvc.perform(delete("/api/todo/1"))
                //THEN
                .andExpect(status().isOk());
    }
}