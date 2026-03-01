package com.marotech.skillhub.action.user;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@SkipAuthentication
@UrlBinding("/web/error")
public class ErrorActionBean extends UserBaseActionBean {

    private static final String ERROR_JSP = "/WEB-INF/jsp/user/error.jsp";

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(ERROR_JSP);
    }

    public String getNavSection() {
        return "home";
    }

}
