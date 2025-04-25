package com.example.springbootproject_1.Service;

import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.repo.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepo todoRepo; //repo here

    public TodoService(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    public List<Todo> getAllTodos() { //this in Controller called
        return todoRepo.findAll(); //this findAll is default of repo
    }
}


//Controller <= Service <= repo