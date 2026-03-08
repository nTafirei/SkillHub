package com.marotech.skillhub.action.user.outbox;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Notification;
import com.marotech.skillhub.model.User;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

@SkipAuthentication
@UrlBinding("/web/send-message")
public class SendMessageActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromName;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromEmail;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromMobile;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String subject;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String body;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true, converter = UserConverter.class)
    private User recipient;

    @DefaultHandler
    public Resolution view() throws Exception {
        return new ForwardResolution(COMPOSE_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {

        Notification notification = new Notification();
        notification.setBody(body);
        notification.setFromName(fromName);
        notification.setFromEmail(fromEmail);
        notification.setFromMobile(fromMobile);
        notification.setSubject(subject);
        notification.setToEmail(recipient.getEmail());
        notification.setToName(recipient.getFullName());
        notification.setToMobile(recipient.getMobilePhone());

        if (recipient != null && recipient.getId() != null) {
            notification.setRecipient(recipient);
        }
        repositoryService.save(notification);

        template = context.createProducerTemplate();
        template.sendBody(config.getProperty("sms.endpoint.url"), notification);
        template.sendBody(config.getProperty("email.endpoint.url"), notification);
        return new RedirectResolution(NEXT_PAGE);
    }

    @Override
    public String getNavSection() {
        return "talent";
    }

    @Override
    protected String getErrorPage() {
        return COMPOSE_JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    @SpringBean
    private CamelContext context;
    private ProducerTemplate template;
    public static final String NEXT_PAGE = "/web/sent";
    public static final String SENT = "sent";

    public static final String COMPOSE_JSP = "/WEB-INF/jsp/user/outbox/compose.jsp";
}
