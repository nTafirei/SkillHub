package com.marotech.skillhub.action.user.innovators;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Showcase;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import java.util.ArrayList;


@UrlBinding("/web/innovators/{user}/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class InnovatorsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    private int currPage = 0;
    @Getter
    private Iterable<User
            > workers = new ArrayList<>();
    @Getter
    @Setter
    @Validate(required = true, on = {DISABLE, ADD}, converter = UserConverter.class)
    private User user;

    @DefaultHandler
    public Resolution list() {
        fetchInnovators();
        return new ForwardResolution(AUTHORS_LIST_JSP);
    }

    @HandlesEvent(REMOVE)
    public Resolution remove() {
        user.setShowcase(Showcase.NO);
        repositoryService.save(user);
        return list();
    }

    @HandlesEvent(ADD)
    public Resolution add() {
        user.setShowcase(Showcase.YES);
        repositoryService.save(user);
        return list();
    }

    private void fetchInnovators() {
        int perPage = config.getIntegerProperty("app.items.per.page");
        int start = currPage * perPage;
        if (currPage == 0) {
            start = 1;
        }
        workers = repositoryService.findInnovators(start, perPage);
    }

    public long getUsersSize() {
        return workers.spliterator().getExactSizeIfKnown();
    }

    @Override
    protected String getErrorPage() {
        return AUTHORS_LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "innovators";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String AUTHORS_LIST_JSP = "/WEB-INF/jsp/user/innovators/list.jsp";
}
