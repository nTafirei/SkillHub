package com.marotech.skillhub.action;

import com.marotech.skillhub.util.Constants;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@SkipAuthentication
@UrlBinding("/web/logout")
public class LogoutActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution view() {
        setCurrentUser(null);
        getContext().getRequest().getSession()
                .setAttribute(Constants.ROLE_ERROR_MESSAGE, null);
        return new RedirectResolution("/web/login");
    }
}
