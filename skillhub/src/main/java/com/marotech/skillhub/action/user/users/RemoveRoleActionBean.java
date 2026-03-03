package com.marotech.skillhub.action.user.users;

import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.action.user.converters.UserRoleConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.UserRole;
import com.marotech.skillhub.util.Constants;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.Iterator;

@UrlBinding("/web/remove-role/{user}/{userRole}/{_eventName}")
public class RemoveRoleActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(converter = UserConverter.class, required = true)
    private User user;
    @Getter
    @Setter
    @Validate(on = Constants.DEMOTE, converter = UserRoleConverter.class, required = true)
    private UserRole userRole;

    @Getter
    private Iterable<UserRole> roles;

    @DefaultHandler
    public Resolution list() {
        roles = getCurrentUser().getUserRoles();
        return new ForwardResolution(CHOOSE_ROLE_JSP);
    }

    @HandlesEvent(Constants.DEMOTE)
    public Resolution demote() {
        Iterator<UserRole> it = user.getUserRoles().iterator();
        while (it.hasNext()) {
            UserRole tmp = it.next();
                if (userRole.getId().equals(tmp.getId())) {
                    it.remove();
                    break;
            }
        }
        repositoryService.save(user);
        return new RedirectResolution(USER_DETAILS + "/" + user.getId());
    }


    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    public String getNavSection() {
        return "staff";
    }

    @Override
    protected String getErrorPage() {
        return CHOOSE_ROLE_JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String USER_DETAILS = "/web/user-details";
    public static final String CHOOSE_ROLE_JSP = "/WEB-INF/jsp/user/users/remove-role.jsp";
}