package com.marotech.skillhub.llm;


import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.embeddings.OllamaEmbedResponseModel;

import java.io.IOException;
import java.util.List;

public abstract class ChatService {

    public abstract OllamaEmbedResponseModel embed(String model, List<String> inputs)
            throws IOException, InterruptedException, OllamaBaseException;

    public abstract String getResponse(String model, String message);

    public abstract String getResponse(String model, String message, OllamaChatMessageRole role);
}
