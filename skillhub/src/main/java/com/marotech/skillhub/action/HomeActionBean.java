package com.marotech.skillhub.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@SkipAuthentication
@UrlBinding("/web/home")
public class HomeActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(HOME_JSP);
    }

    public static final String HOME_JSP = "/WEB-INF/jsp/user/home.jsp";
}
