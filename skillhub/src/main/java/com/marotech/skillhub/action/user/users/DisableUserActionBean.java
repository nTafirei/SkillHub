package com.marotech.skillhub.action.user.users;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/disable-user/{user}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class DisableUserActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(on = DISABLE, converter = UserConverter.class, required = true)
    private User user;

    @DefaultHandler
    public Resolution view() {
        user.setActiveStatus(ActiveStatus.NOT_ACTIVE);
        repositoryService.save(user);
        return new RedirectResolution(UsersActionBean.USERS_LIST);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return USER_DETAIL_JSP;
    }

    @Override
    public String getNavSection() {
        return "users";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String USER_DETAIL_JSP = "/WEB-INF/jsp/user/users/user-details.jsp";
}
