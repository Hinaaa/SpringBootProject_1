package com.example.springbootproject_1.Service;

import com.example.springbootproject_1.Exceptions.TodoNotFoundException;
import com.example.springbootproject_1.Model.Todo;
import com.example.springbootproject_1.Model.TodoDto;
import com.example.springbootproject_1.repo.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepo todoRepo; //repo here
    private final IdService idService;
    private final ChatGPTService chatGPTService; // Add this line //ADDED FOR Chat gtp Service Class

    public TodoService(TodoRepo todoRepo, IdService idService,ChatGPTService chatGPTService) {
        this.todoRepo = todoRepo;
        this.idService = idService;
        this.chatGPTService = chatGPTService;
    }
    //list of all todo
    public List<Todo> getAllTodos() { //this in Controller called
        return todoRepo.findAll(); //this findAll is default of repo
    }
    //get by id //updated with throw exception //Updated to use custom exception
    public Todo getTodoById(String id) {
        return todoRepo.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo" +id+ " not found"));//throwing this custom exceptin: TodoNotFoundException
    }
    //post todo
    public Todo addTodo(TodoDto newtodoDto) {
        String checkedSpellings = chatGPTService.getOpenAiSpellingCheck(newtodoDto.description());
        Todo todo = new Todo(
                idService.generateId(),
                checkedSpellings, // use corrected text
                newtodoDto.status());
        todoRepo.save(todo);
        return todo;
    }
    //update
    public Todo updateTodo(String id, TodoDto newtodoDto) {
        if (todoRepo.existsById(id)) {
            Todo updatedTodo = new Todo(id, newtodoDto.description(), newtodoDto.status());
            return todoRepo.save(updatedTodo); // updated todo
        }
        throw new TodoNotFoundException("Todo not found");
    }
    //delete deleteTodoById
    public boolean deleteTodoById(String id) {
        if (todoRepo.existsById(id)) {
            todoRepo.deleteById(id);
            return true;
        } else
            throw new TodoNotFoundException("Todo not found");
    }
}


//Controller <= Service <= repo