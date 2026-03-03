package com.marotech.skillhub.action.user.talent;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.util.Constants;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@UrlBinding("/web/talent/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class TalentActionBean extends UserBaseActionBean {

    @Getter
    private List<User> users = new ArrayList<>();
    @Getter
    @Setter
    @Validate(on = DISABLE, converter = UserConverter.class, required = true)
    private User user;

    @DefaultHandler
    public Resolution list() {

        Iterable<User> temp =
                repositoryService.fetchAllUsersWithRoles(Arrays.asList(ROLES));

        for (User user1 : temp) {
            if (!user1.hasRole(AGENT) && !user1.hasRole(AGENT_SUPERVISOR)) {
                users.add(user1);
            }
        }

        return new ForwardResolution(USERS_LIST_JSP);
    }

    @HandlesEvent(DISABLE)
    public Resolution disable() {
        return new RedirectResolution(USERS_LIST);
    }

    public long getUsersSize() {
        return users.spliterator().getExactSizeIfKnown();
    }

    @Override
    protected String getErrorPage() {
        return USERS_LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "users";
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String USERS_LIST = "/web/talent";

    private static final String [] ROLES = {Constants.TALENT};
    private static final String USERS_LIST_JSP = "/WEB-INF/jsp/user/talent/list.jsp";
}
