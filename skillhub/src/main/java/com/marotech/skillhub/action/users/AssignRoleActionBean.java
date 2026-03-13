package com.marotech.skillhub.action.users;

import com.marotech.skillhub.action.RequiresOneRoleOf;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.UserConverter;
import com.marotech.skillhub.action.converters.UserRoleConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.Iterator;

@UrlBinding("/web/assign-role/{user}/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class AssignRoleActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(required = true, converter = UserConverter.class)
    private User user;
    @Getter
    @Setter
    @Validate(on = {PROMOTE, DEMOTE}, required = true, converter = UserRoleConverter.class)
    private UserRole userRole;

    @Getter
    private Iterable<UserRole> roles;

    @DefaultHandler
    public Resolution list() {
        roles = repositoryService.findAllRoles();
        Iterator<UserRole> it = roles.iterator();
        while (it.hasNext()) {
            UserRole userRole1 = it.next();
            for (UserRole tmp : user.getUserRoles()) {
                if (userRole1.getId().equals(tmp.getId())) {
                    it.remove();
                }
            }
        }
        return new ForwardResolution(CHOOSE_ROLE_JSP);
    }

    @HandlesEvent(PROMOTE)
    public Resolution promote() {
        user.addUserRole(userRole);
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
        if(user.getIsTalent()){
            return "talent";
        }
        return "users";
    }

    @Override
    protected String getErrorPage() {
        return CHOOSE_ROLE_JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String DEMOTE = "demote";
    public static final String PROMOTE = "promote";
    public static final String USER_DETAILS = "/web/user-details";
    public static final String CHOOSE_ROLE_JSP = "/WEB-INF/jsp/user/users/choose-role.jsp";
}