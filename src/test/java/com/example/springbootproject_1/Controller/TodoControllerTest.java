package com.example.springbootproject_1.Controller;


import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.Model.TodoStatus;
import com.example.springbootproject_1.repo.TodoRepo;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepo todoRepository;

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
        //return empty list
    void getAllTodos_shouldReturn_EmptyList_WhenCalledInitially() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo")) //faking/simulate an HTTP GET request to /api/todo using MockMvc.
                .andExpect(MockMvcResultMatchers.status().isOk()) //hat the response status is 200 OK.
                .andExpect(MockMvcResultMatchers.content().json("""
                        []
                    """));//Asserts response body is empty JSON array ([])
    }

    @Test
    void getAllTodos_shouldReturn_ListOfAllTodos_WhenCalled() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [{
                        "id": "1",
                        "description":"Read Book",
                        "status": "OPEN"
                        }]
                """
                ));
    }

    @Test
    void getAllTodos_shouldReturn_TodoWithId1_WhenCalledWithId1() throws Exception {
        Todo exisitngTodo = new Todo("1", "Read Book", TodoStatus.OPEN);
        todoRepository.save(exisitngTodo);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [{
                        "id": "1",
                        "description":"Read Book",
                        "status": "OPEN"
                        }]
                """
                ));
    }
    @Test //call by id which not exists
    void getProductById_shouldThrowIdNotFoundException_whenCalledForInvalidId() throws Exception {
        String invalidId = "99";
        mockMvc.perform(get("api/todo/"+ invalidId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                        .value("Error: Todo "+invalidId+" not found"));
    }
    // POST request to /api/todo with JSON content describing the new Todo
    @Test
    void postTodos_shouldReturnTodo_WhenCalledWithDto() throws Exception {
//        Todo exisitngTodo = new Todo("2", "Eat Food", TodoStatus.OPEN);
//        todoRepository.save(exisitngTodo);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON) //Setting content type to JSON
                        .content("""
                                {
                                    "description": "Eat Food",
                                    "status": "OPEN"
                                }
                            """) // JSON payload representing the new Todo
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                                {
                                    "description": "Eat Food",
                                    "status": "OPEN"
                                }
                            """))// Expect the response JSON content to have the same description and status as sent
                .andExpect(jsonPath("$.id").isNotEmpty()) //// Expect the response JSON to contain a non-empty 'id' field
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    //update
    @Test
    void updateTodo_shouldReturnUpdated_whenCalledWithId() throws Exception {
        Todo existingTodo = new Todo("1", "Read Book", TodoStatus.OPEN);
        todoRepository.save(existingTodo);
        //When
        mockMvc.perform(put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "Read Book",
                                "status": "IN_PROGRESS"
                                 }
                                """))

                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                "description": "Read Book",
                                "status": "IN_PROGRESS"
                                 }
                                """));
    }
    @Test
    void updateTodo_shouldThrowIdNotFoundException_whenCalledForInvalidId() throws Exception {
        String invalidId = "99";
        mockMvc.perform(put("/api/todo/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                    "description": "Read Book",
                    "status": "IN_PROGRESS"
                    }
                    """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                        .value("Error: Todo " + invalidId + " not found"));
    }
    //delete
    @Test
    void deleteTodo_shouldDeleteDataFromDBAndReturnTrue_whenCalledWithValidId() throws Exception {

        //Given
        Todo existingTodo = new Todo("1", "Read Book", TodoStatus.OPEN);
        todoRepository.save(existingTodo);
        //When
        mockMvc.perform(delete("/api/todo/1"))
        //Then
                .andExpect(status().isOk());
    }
    @Test
    void deleteTodo_shouldThrowIdNotFoundException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "99";

        mockMvc.perform(delete("/api/todo/" + invalidId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                        .value("Error: Todo " + invalidId + " not found"));
    }
}

//actual is response of controller. mockMvc.perform(...) sends the fake request
//Then //expected values are provided inside .andExpect() calls.