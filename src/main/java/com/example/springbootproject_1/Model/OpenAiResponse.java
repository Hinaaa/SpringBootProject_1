package com.example.springbootproject_1.Model;

import java.util.List;

public record OpenAiResponse(List<OpenAiOutput> output) {
    public String answer() {
        return output.get(0)
                .content().get(0)
                .text();
    }
}
