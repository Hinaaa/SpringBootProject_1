package com.example.springbootproject_1.repo;

import com.example.springbootproject_1.Model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepo extends MongoRepository<Todo,String> {
}
