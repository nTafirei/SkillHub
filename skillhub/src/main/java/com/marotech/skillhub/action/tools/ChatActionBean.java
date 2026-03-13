package com.marotech.skillhub.action.tools;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.llm.ChatService;
import com.marotech.skillhub.llm.MockChatService;
import com.marotech.skillhub.llm.OllamaChatService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@SkipAuthentication
@UrlBinding("/web/chat")
public class ChatActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(on = SEARCH, required = true)
    private String request;
    @Getter
    private String answer = "I am not sure how to respond to that";

    @DefaultHandler
    public Resolution list() {
        answer = "";
        return new ForwardResolution(LIST_JSP);
    }

    @HandlesEvent(SEARCH)
    public Resolution request() {
        setChatService();
        String model = config.getProperty("platform.active.model");
        answer = chatService.getResponse(model, request);
        return new ForwardResolution(LIST_JSP);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    private void setChatService() {
        String languageModel = config.getProperty("platform.language.service.option");
        if ("mock".equalsIgnoreCase(languageModel)) {
            chatService = mockChatService;
        } else {
            chatService = ollamaChatService;
        }
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "tools";
    }

    private ChatService chatService;
    @SpringBean
    private RepositoryService repositoryService;
    @SpringBean
    private MockChatService mockChatService;
    @SpringBean
    private OllamaChatService ollamaChatService;
    private static final String LIST_JSP = "/WEB-INF/jsp/user/tools/chat.jsp";
}
