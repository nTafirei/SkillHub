package com.marotech.skillhub.components.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.gson.CustomExclusionStrategy;
import com.marotech.skillhub.llm.ChatService;
import com.marotech.skillhub.llm.MockChatService;
import com.marotech.skillhub.llm.OllamaChatService;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.util.TextChunker;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.embeddings.OllamaEmbedResponseModel;
import jakarta.annotation.PostConstruct;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class PublicationFileProcessor implements Processor {

    private String languageModel;
    private Integer minLength;
    private Integer maxLength;
    private Integer overlap;
    private String boundary;
    private String delimiters;
    private Tika tika = new Tika();

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = exchange.getProperty("fileName", String.class);
        File file = exchange.getIn().getBody(File.class);
        String contentType = exchange.getIn().getHeader("Content-Type", String.class);
        if (StringUtils.isBlank(contentType)) {
            contentType = tika.detect(file);
            exchange.getIn().setHeader("Content-Type", contentType);
        }
        String body = exchange.getIn().getBody(String.class);
        //TODO
        //1. extract book/article metadata such as workers, pub date, ect using LLM
        //2. save to Publication and Worker tables
        //3. vectorise ad save it in PostGreSQL for contextual search

        String prompt = String.valueOf(readFileFromClasspath("artifact-prompt.txt"));
        prompt = prompt.replace(ARTIFACT_DATA_HERE, body);
        String response = chatService.getResponse(languageModel, prompt);
        Publication pub = extractAndSavePublication(fileName, response, contentType);
        vectorise(pub, body);
        List<User> users =
                repositoryService.fetchAllUsersWithRoles(Arrays.asList(ROLES));
        sendNotifications(users, pub);
    }

    private Publication extractAndSavePublication(String filename, String response,
                                                  String contentType) {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new CustomExclusionStrategy())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        Type type = new TypeToken<JsonPub>() {
        }.getType();
        JsonPub jsonPub = gson.fromJson(response, type);

        Publication pub = new Publication();
        pub.setFileName(filename);
        Attachment attachment = new Attachment();
        attachment.setData(response.getBytes());
        attachment.setContentType(contentType);
        attachment.setSize(response.getBytes().length);
        attachment.setName(filename);
        repositoryService.save(attachment);

        pub.setAttachment(attachment);
        pub.setTitle(jsonPub.getTitle());
        pub.setSource(jsonPub.getSource());
        pub.setCategory(Category.valueOf(jsonPub.getCategory()));
        pub.setPubDate(jsonPub.getPublication_date());

        for (JsonPub.Citation citation : jsonPub.getCitations()) {
            pub.getCitations().add(citation.getCitation());
        }

        for (JsonPub.Worker worker : jsonPub.getWorkers()) {
            String name = worker.getWorker();
            String[] names = name.split(" ");
            String firstName = names[0];
            StringBuilder lastNames = new StringBuilder();
            for (int i = 1; i < names.length; i++) {
                lastNames.append(names[i]).append(" ");
            }
            //TODO: there could be more than 1 worker with the same name!!!
            List<Worker> theWorkers = repositoryService.findWorkerByNames(firstName,
                    lastNames.toString());
            if (!theWorkers.isEmpty()) {
                pub.getWorkers().addAll(theWorkers);
            } else {
                Worker worker1 = new Worker();
                worker1.setFirstName(firstName);
                worker1.setLastName(lastNames.toString());
                repositoryService.save(worker1);
                pub.getWorkers().add(worker1);
            }
        }

        repositoryService.save(pub);
        return pub;
    }

    private void vectorise(Publication pub, String body) throws IOException,
            OllamaBaseException, InterruptedException {
        String summary = chatService.getResponse(languageModel,
                SUMMARIZE_PROMPT.replaceAll(TEXT_HERE, body));
        pub.setSummary(summary);
        repositoryService.save(pub);
        TextChunker.SplitOptions options = new TextChunker.SplitOptions(20, 40,
                5, PARAGRAPH, " ");
        TextChunker chunker = new TextChunker();
        String[] chunks = chunker.chunkText(body, options);
        List<String> inputs = Arrays.asList(chunks);
        OllamaEmbedResponseModel responseModel = chatService.embed(languageModel, inputs);
        if (responseModel != null) {
            List<List<Double>> embeddings = responseModel.getEmbeddings();
            int index = 0;
            for (List<Double> embedding : embeddings) {
                Embedding embedding1 = new Embedding();
                embedding1.setPublication(pub);
                embedding1.setChunk(inputs.get(index));
                embedding1.setEmbedding(embedding);
                repositoryService.save(embedding1);
                index = index + 1;
            }
        }
    }

    @PostConstruct
    protected void fetchChatService() {
        languageModel = config.getProperty("platform.language.service.option");
        if (MOCK.equalsIgnoreCase(languageModel)) {
            chatService = mockChatService;
        } else {
            chatService = ollamaChatService;
        }
        minLength = config.getIntegerProperty("splitter.minLength");
        maxLength = config.getIntegerProperty("splitter.maxLength");
        overlap = config.getIntegerProperty("splitter.overlap");
        boundary = config.getProperty("splitter.boundary");
        delimiters = config.getProperty("splitter.delimiters");
    }

    private byte[] readFileFromClasspath(String resourcePath) throws IOException {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return in.readAllBytes();
        }
    }

    private void sendNotifications(List<User> users, Publication publication) {
        for (User user : users) {
            Notification notification = new Notification();
            notification.setRecipient(user);
            notification.setOrg(user.getOrg());
            notification.setSubject("Publication " + publication.getTitle() + " has been processed");
            notification.setBody("Publication " + publication.getTitle()
                    + " has been processed at " + LocalDateTime.now());
            repositoryService.save(notification);
        }
    }

    @Autowired
    protected MockChatService mockChatService;
    @Autowired
    protected OllamaChatService ollamaChatService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected Config config;
    protected ChatService chatService;
    public static final String PARAGRAPH = "paragraph";
    public static final String TEXT_HERE = "TEXT_HERE";
    public static final String MOCK = "mock";
    public static final String ARTIFACT_DATA_HERE = "ARTIFACT_DATA_HERE";
    private static final String[] ROLES = {"Administrator", "System Administrator"};

    private String SUMMARIZE_PROMPT = "Summarize the following text in concise and clear manner, " +
            "capturing the main points and key information in 300 words or less:\n TEXT_HERE";
}
