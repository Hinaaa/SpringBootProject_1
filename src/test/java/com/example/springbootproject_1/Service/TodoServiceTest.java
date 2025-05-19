package com.example.springbootproject_1.Service;

import com.example.springbootproject_1.Exceptions.TodoNotFoundException;
import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.Model.TodoDto;
import com.example.springbootproject_1.Model.TodoStatus;
import com.example.springbootproject_1.repo.TodoRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {
    TodoRepo mockRepo = Mockito.mock(TodoRepo.class); //Creates a mock of TodoRepo — not real, used to fake behavior.
    IdService mockIdService = Mockito.mock(IdService.class); // Creates a mock of IdService
    ChatGPTService mockChatGPTService = Mockito.mock(ChatGPTService.class);

    @Test
    void getAllTodos_shouldReturnEmptyList_whenCalledWithDto() {
        //given
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService); // Injects the mocks into the service — so it doesn’t call real code.
        List<Todo> expected = Collections.emptyList(); // Prepares the expected result (an empty list).
        //When
        List<Todo> actual = todoService.getAllTodos(); // what to return is not defined. alls the actual method — BUT since mockRepo is mocked and not stubbed, it returns null by default
        //Then
        assertEquals(expected, actual); //compare
    }

    @Test
    void getAllTodos_shouldReturnData_whenCalledWithData() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Todo todo1 = new Todo("1", "Book Read", TodoStatus.OPEN); //Created a sample Todo and placed it into a list as expected output.
        List<Todo> expected = List.of(todo1); //expected
        Mockito.when(mockRepo.findAll()).thenReturn(expected);//Telling the mock, When findAll() called, return the expected fake list

        //When
        List<Todo> actual = todoService.getAllTodos(); //// Step 4: Call the real service method — it uses the fake repo
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void getAllTodos_shouldReturnData_whenCalledWithId() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Todo todo2 = new Todo("2", "Cook Food", TodoStatus.OPEN); //fake Todo
        TodoDto todoDto2 = new TodoDto("Cook Food", TodoStatus.OPEN);
        List<Todo> expected = List.of(todo2);
        Mockito.when(mockRepo.findAll()).thenReturn(expected);// Step 5: Check if the method returned the correct data from the fake repo
        //When
        List<Todo> actual = todoService.getAllTodos();
        //Then
        assertEquals(expected, actual);
    }

    @Test
        //get todo by id
    void getTodoById_shouldReturnData1_whenCalledWithId1() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Todo expected = new Todo("1", "Read Book", TodoStatus.OPEN);
        Mockito.when(mockRepo.findById("1")).thenReturn(Optional.of(expected)); //Mock repo to return expected Todo when id "1" is searched
        //When
        Todo actual = todoService.getTodoById(expected.id());
        //Then
        assertEquals(expected, actual);
    }

    @Test
//get with invalid id
    void getTodoById_shouldThrowException_whenCalledWithInvalidId() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Mockito.when(mockRepo.findById("1")).thenReturn(Optional.empty());
        //When
        try {
            todoService.getTodoById("1");
            //Then
            fail();
        } catch (TodoNotFoundException e) {
            assertTrue(true);
        }
    }

    //Push
    @Test
    void addTodo_shouldReturnAddedData_whenCalledWithDto() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Todo expected = new Todo("3", "Fill Excel", TodoStatus.OPEN);
        TodoDto todoDto3 = new TodoDto("Fill Excel", TodoStatus.OPEN);
        Mockito.when(mockIdService.generateId()).thenReturn("3"); // Mock ID generation
        Mockito.when(mockChatGPTService.getOpenAiSpellingCheck("Fill Excel")).thenReturn("Fill Excel");
        //When
        Todo actual = todoService.addTodo(todoDto3);
        //Then
        assertEquals(expected, actual);
        Mockito.verify(mockRepo).save(expected);
    }

    //update
    @Test
    void updateTodo_shouldReturnAdd_whenCalledWithData() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService); // Create the service instance with mocked dependencies
        TodoDto newTodo = new TodoDto("Fill Excel", TodoStatus.OPEN); // Create the new Todo data to update
        Todo expected = new Todo("3", "Fill Excel", TodoStatus.OPEN);//expected Todo after update
        Mockito.when(mockRepo.existsById("3")).thenReturn(true); //Fake the check that Todo "3" is already there.
        Mockito.when(mockRepo.save(expected)).thenReturn(expected); // Mock saving returns the updated Todo
        //When
        Todo actual = todoService.updateTodo("3", newTodo);//  Perform update operation
        //then
        assertEquals(expected, actual);
        Mockito.verify(mockRepo).save(expected);//Verify save was called once with expected Todo
    }

    @Test
        //when id not found
    void updateTodo_shouldThrowException_whenIdNotFound() {
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Todo expected = new Todo("1", "Read Book", TodoStatus.OPEN);
        TodoDto todoDto1 = new TodoDto("Read Book", TodoStatus.OPEN);
        Mockito.when(mockRepo.existsById("1")).thenReturn(false);
        //When
        try {
            todoService.updateTodo("1", todoDto1);
            fail();
        } catch (TodoNotFoundException e) {
            assertTrue(true);
        }
    }

    //Delete
    @Test
    void deleteTodo_shouldDeleteDataFromDBAndReturnTrue_whenCalledWithValidId() {
        //Given
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Mockito.when(mockRepo.existsById("1")).thenReturn(true);
        //When
        boolean actual = todoService.deleteTodoById("1");
        //Then
        assertTrue(actual);
        Mockito.verify(mockRepo).deleteById("1");
    }

    @Test
    void deleteTodo_shouldThrowException_whenCalledWithInValidId() {
        //Given
        TodoService todoService = new TodoService(mockRepo, mockIdService, mockChatGPTService);
        Mockito.when(mockRepo.existsById("1")).thenReturn(false);
        //When
        try {
            Boolean actual = todoService.deleteTodoById("1");
            fail();
        }
        catch (TodoNotFoundException e) {
            assertTrue(true); //exception caught in this case
        }
        //Mockito.verify(mockRepo).deleteById("1"); //No verify here because deleteById should NOT be called
    }
}
//In unit testing, you're just checking if the method works correctly:
//expected given by me. actual: when method called is it returnig fake data I given as in repo. so bith given
