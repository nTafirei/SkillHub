package com.marotech.skillhub.action.inbox;

import com.marotech.skillhub.action.RequiresOneRoleOf;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.NotificationConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Notification;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.ArrayList;
import java.util.List;

@UrlBinding("/web/inbox")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User", "Customer Service"})
public class InboxActionBean extends UserBaseActionBean {

    @Getter
    private List<Notification> notifications = new ArrayList<>();
    @Getter
    @Setter
    @Validate(on = {EDIT, DISABLE}, converter = NotificationConverter.class, required = true)
    private Notification notification;

    @DefaultHandler
    public Resolution list() {
        return new ForwardResolution(LIST_JSP);
    }

    public long getNotificationsSize() {
        if(notifications == null){
            return 0;
        }
        return notifications.size();
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "inbox";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String LIST_JSP = "/WEB-INF/jsp/user/inbox/list.jsp";
}
