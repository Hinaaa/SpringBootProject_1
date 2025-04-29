package com.example.springbootproject_1.Service;

import com.example.springbootproject_1.Model.OpenAiRequest;
import com.example.springbootproject_1.Model.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

        @Service
        public class ChatGPTService {

            private final RestClient restClient;
            //contructor
            public ChatGPTService(RestClient.Builder restClientBuilder, @Value("${API_KEY}") String apiKey) {
                this.restClient = restClientBuilder
                        .baseUrl("https://api.openai.com/v1/chat/completions")
                        .defaultHeader("Authorization", "Bearer " + apiKey)
                        .build();
            }
            //spelling
            public String getOpenAiSpellingCheck(String text) {
                OpenAiRequest request = new OpenAiRequest("gpt-4.1",
                        "Can you correct the word and respond with just one word? " + text);
                OpenAiResponse response = restClient.post()//POST request to the OpenAI API with restClient object
                        .contentType(MediaType.APPLICATION_JSON)// Setting the content type of the request to JSON
                        .body(request) //body request set to OpenAiRequest
                        .retrieve()
                        .body(OpenAiResponse.class);
                return response.answer(); // Returning response result
            }
        }