package com.marotech.skillhub.action.user.users;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/user-details/{user}/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class UserDetailsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = UserConverter.class, required = true)
    private User user;

    @DefaultHandler
    public Resolution view() {
            return new ForwardResolution(USER_DETAIL_JSP);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static final String USER_DETAIL_JSP = "/WEB-INF/jsp/user/users/user-details.jsp";
}
