package com.marotech.skillhub.llm;

import com.marotech.skillhub.components.config.Config;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;
import io.github.ollama4j.models.embeddings.OllamaEmbedResponseModel;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaChatService extends ChatService {

    @Autowired
    private Config config;
    private OllamaAPI ollamaAPI;
    private boolean valid = true;
    private String llmPlatform;
    private String ollamaHost;

    private Map<String, OllamaChatRequestBuilder> builders = new HashMap<>();

    @PostConstruct
    public void build() {
        ollamaHost = config.getProperty("platform.ollama.host");
        if (StringUtils.isBlank(ollamaHost)) {
            LOG.error("Could not resolve Ollama host. Will not continue");
            valid = false;
            return;
        }
        ollamaAPI = new OllamaAPI(ollamaHost);
        //registerFunctionTools();
    }

    @Override
    public String getResponse(String model, String message, OllamaChatMessageRole role) {
        if (!valid) {
            LOG.error("Could not resolve Ollama host. Will not continue");
        }

        if (StringUtils.isBlank(model)) {
            LOG.error("Could not resolve Ollama model in message. Will not continue");
            return null;
        }

        OllamaChatRequestBuilder builder = builders.get(model);
        if (builder == null) {
            builder = OllamaChatRequestBuilder.getInstance(llmPlatform);
            if (builder == null) {
                LOG.error("Could not get LLM {} from Ollama server. Will not continue", model);
                return null;
            }
            builders.put(model, builder);
        }

        OllamaChatRequest requestModel = builder.withMessage(role, message).build();

        // converse with the model
        try {
            OllamaChatResult chatResult = ollamaAPI.chat(requestModel);
            return chatResult.getResponseModel().getMessage().getContent();
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return null;
    }


    @Override
    public OllamaEmbedResponseModel embed(String model, List<String> inputs) throws IOException, InterruptedException, OllamaBaseException {
        return ollamaAPI.embed(model, inputs);
    }

    @Override
    public String getResponse(String model, String message) {
        return getResponse(model, message, OllamaChatMessageRole.USER);
    }

    private static final Logger LOG = LoggerFactory.getLogger(OllamaChatService.class);
}
