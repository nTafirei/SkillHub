package com.marotech.skillhub.action.user.users;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.util.Constants;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SkipAuthentication
@UrlBinding("/web/register2")
public class RegisterActionBeanPart extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String password;
    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String firstName;
    @Getter
    @Setter
    @Validate(on = {SAVE})
    private String middleName;
    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String lastName;
    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String nationalId;
    @Getter
    @Setter
    @Validate
    private String fax;
    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String address;
    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String town;
    @Getter
    @Setter
    @Validate
    private String telephone;
    @Getter
    @Setter
    @Validate(on = {SAVE}, required = true)
    private String mobilephone;

    @DefaultHandler
    public Resolution view() {
        getContext().getRequest().getSession()
                .setAttribute(Constants.LOGGED_IN_USER, null);
        return new ForwardResolution(REGISTER2_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {

        boolean exists = repositoryService.findUserByNationalId(nationalId) != null;
        if (exists) {
            getContext().getValidationErrors().add("username",
                    new LocalizableError("userexists"));

            return new ForwardResolution(REGISTER2_JSP);
        }

        AuthUser authUser = new AuthUser();
        mobilephone = mobilephone.replaceAll(" ", "");
        authUser.setUserName(mobilephone.toLowerCase());
        String newPassword = AuthUser.encodedPassword(password);
        authUser.setPassword(newPassword);
        repositoryService.save(authUser);

        String country = config.getProperty("country");

        User user = new User(firstName,
                middleName, lastName, address,
                town, mobilephone, nationalId, country);
        String orgName = config.getProperty("orgId");
        repositoryService.save(user);
        authUser.setSystemUser(user);
        repositoryService.save(authUser);

        getContext().getRequest().getSession()
                .setAttribute(Constants.LOGGED_IN_USER, user);

        try {
            repositoryService.save(user);
        } catch (Exception ex) {
            LOG.error("Error saving user record", ex);
            repositoryService.getRepository().delete(user);
            repositoryService.getRepository().delete(authUser);
            getContext().getValidationErrors().add("username",
                    new LocalizableError("usersavefailed"));
            return new ForwardResolution(REGISTER2_JSP);
        }

        UserRole role = repositoryService.findUserRoleByRoleName("User");
        user.getUserRoles().add(role);
        repositoryService.save(user);

        return new RedirectResolution(INBOX_LIST);
    }

    @Override
    public String getNavSection() {
        return "register";
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return REGISTER2_JSP;
    }

    public Gender[] getGenders() {
        return Gender.values();
    }

    public String[] getCountries() {
        return config.getProperty("system.available.countries").split(",");
    }

    @SpringBean
    private RepositoryService repositoryService;

    private static final String INBOX_LIST = "/web/inbox/list";
    private static final String REGISTER2_JSP = "/WEB-INF/jsp/user/users/register2.jsp";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterActionBeanPart.class);
}
