package com.example.springbootproject_1.Controller;

import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.Model.TodoDto;
import com.example.springbootproject_1.Service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    //object creation of service
    private final TodoService todoService;
    //contructor of todo sevice
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    //method: Get
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos(); //this getAllTodos() is defined in service class
    }
    //post
    @PostMapping
    public Todo addTodo(@RequestBody TodoDto todoDto) {
        return todoService.addTodo(todoDto);
    }
    //Get BY id
    @GetMapping ("/{id}")
    public Todo getById(@PathVariable String id) {
        return todoService.getTodoById(id);
    }
    //put
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody TodoDto todoDto) {
        return todoService.updateTodo(id,todoDto);
    }
    //delete
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteTodoById(id);
    }

}
