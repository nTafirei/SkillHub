package com.marotech.skillhub.action.user.outbox;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Notification;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

@SkipAuthentication
@UrlBinding("/web/message{_eventName}")
public class MessageActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String mobile;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String name;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String subject;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String message;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String email;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private User recipient;
    @DefaultHandler
    public Resolution save() throws Exception {

            Notification notification = new Notification();
            notification.setBody(message);
            notification.setFrom(name);
            notification.setEmail(email);
            notification.setMobile(mobile);
            notification.setSubject(subject);
            notification.setRecipient(recipient);
            repositoryService.save(notification);

        return new RedirectResolution(NEXT_PAGE);
    }

    @HandlesEvent(SENT)
    public Resolution sent() throws Exception {
        return new ForwardResolution(JSP);
    }

    @Override
    public String getNavSection() {
        return "talent";
    }

    @Override
    protected String getErrorPage() {
        return JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String NEXT_PAGE = "/web/message/sent";
    public static final String SENT = "sent";
    public static final String JSP = "/WEB-INF/jsp/user/outbox/sent.jsp";
}
