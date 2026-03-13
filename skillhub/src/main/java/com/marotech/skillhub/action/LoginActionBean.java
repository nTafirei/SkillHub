package com.marotech.skillhub.action;

import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.AuthUser;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.util.Constants;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.apache.commons.lang.StringUtils;

@SkipAuthentication
@UrlBinding("/web/login")
public class LoginActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    private User user;
    @Getter
    @Setter
    @Validate(on = LOGIN, required = true)
    private String username;
    @Getter
    @Setter
    @Validate(on = LOGIN, required = true)
    private String password;
    @Getter
    @Setter
    @Validate(on = LOGIN)
    private String target;

    @DefaultHandler
    public Resolution view() {
        String roleErrorMessage = (String) getContext()
                .getRequest().getSession()
                .getAttribute(Constants.ROLE_ERROR_MESSAGE);
        if (StringUtils.isNotBlank(roleErrorMessage)) {
            getContext().getValidationErrors().add("username",
                    new SimpleError(roleErrorMessage));
            getContext().getRequest().getSession()
                    .setAttribute(Constants.ROLE_ERROR_MESSAGE, null);
        }
        return new ForwardResolution(WEB_INF_JSP_LOGIN_JSP);
    }

    @HandlesEvent(LOGIN)
    public Resolution login() throws Exception {

        username = username.replaceAll(" ", "");
        AuthUser authUser = repositoryService.findAuthUserByUserName(username.toLowerCase());

        if (authUser == null) {
            getContext().getValidationErrors().add("username",
                    new LocalizableError("usernotfound"));
            return new ForwardResolution(WEB_INF_JSP_LOGIN_JSP);
        }

        String newPassword = AuthUser.encodedPassword(password);
        if (!newPassword.equals(authUser.getPassword())) {
            getContext().getValidationErrors().add("username",
                    new LocalizableError("invalidcredentials"));
            return new ForwardResolution(WEB_INF_JSP_LOGIN_JSP);
        }

        user = authUser.getUser();
        getContext().getRequest().getSession()
                .setAttribute(Constants.LOGGED_IN_USER, user);

        if (!StringUtils.isBlank(target)) {
            return new RedirectResolution(target);
        }

        return new RedirectResolution(NEXT_PAGE);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    public String getNavSection() {
        return "login";
    }

    @Override
    protected String getErrorPage() {
        return WEB_INF_JSP_LOGIN_JSP;
    }


    @SpringBean
    private RepositoryService repositoryService;

    public static final String LOGIN = "login";
    public static final String NEXT_PAGE = "/web/inbox";
    public static final String WEB_INF_JSP_LOGIN_JSP = "/WEB-INF/jsp/user/login.jsp";
}
