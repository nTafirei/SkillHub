package com.marotech.skillhub.action.users;


import com.marotech.skillhub.action.RequiresOneRoleOf;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.Verified;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/verify-user/{user}/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class VerifyUserActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = UserConverter.class, required = true)
    private User user;

    @HandlesEvent(VERIFY)
    public Resolution verify() {
        user.setVerified(Verified.YES);
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
    protected String getErrorPage() {
        return VERIFY_JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String VERIFY = "verify";
    public static final String USER_DETAILS = "/web/user-details";
    public static final String VERIFY_JSP = "/WEB-INF/jsp/user/users/verify.jsp";
}