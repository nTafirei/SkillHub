package com.marotech.skillhub.llm;

import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.embeddings.OllamaEmbedResponseModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MockChatService extends ChatService {
    private final Map<String, String> mockResponses;

    public MockChatService() {
        mockResponses = new HashMap<>();
        mockResponses.put("Tell me a joke", "Why don't scientists trust atoms? Because they make up everything!");
        mockResponses.put("What is Java?", "Java is a high-level, class-based, object-oriented programming language.");
    }

    public String getResponse(String model, String message, OllamaChatMessageRole role) {
        return getResponse(model, message);
    }

    @Override
    public OllamaEmbedResponseModel embed(String model, List<String> inputs) throws IOException, InterruptedException, OllamaBaseException {
        return null;
    }

    public String getResponse(String model, String message) {
        if (message.contains("joke")) {
            return mockResponses.get("Tell me a joke");
        } else if (message.contains("Java")) {
            return mockResponses.get("What is Java?");
        } else if (message.contains("president")) {
            return mockResponses.get("Who is the president of the USA?");
        }
        return mockResponses.getOrDefault(message, "I'm not sure how to respond to that.");
    }
}
