package com.marotech.skillhub.action.users;

import com.marotech.skillhub.action.RequiresOneRoleOf;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/search-users")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class SearchUsersActionBean extends UserBaseActionBean {

    @Getter
    private User user;
    @Getter
    @Setter
    @Validate(on = SEARCH)
    private String mobilePhone;
    @Getter
    @Setter
    @Validate(on = SEARCH)
    private String nationalId;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(SEARCH_JSP);
    }

    @HandlesEvent(SEARCH)
    public Resolution search() throws Exception {

        user = repositoryService.findUserByMobilePhone(mobilePhone);
        if (user == null) {
            user = repositoryService.findUserByNationalId(nationalId);
        }

        if (user == null) {
            getContext().getValidationErrors().add("mobilePhone",
                    new LocalizableError("mobilephonenotfound"));
            getContext().getValidationErrors().add("email",
                    new LocalizableError("emailnotfound"));
            return new ForwardResolution(SEARCH_JSP);
        }

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
        return SEARCH_JSP;
    }

    @Override
    public String getNavSection() {
        return "users";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String SEARCH_JSP = "/WEB-INF/jsp/user/users/search.jsp";
    private static final String USER_DETAILS = "/web/user-details";

}
