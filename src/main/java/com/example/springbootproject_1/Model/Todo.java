package com.example.springbootproject_1.Model;

import org.springframework.data.annotation.Id;

public record Todo(@Id String id, String title, boolean completed) {
}
