package com.example.springbootproject_1.Controller;

import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.Service.TodoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    //object creation of service
    private final TodoService todoService;
    //contryuctor of todo sevice
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    //method: Get
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos(); //this getAllTodos() is defined in service class
    }
}
