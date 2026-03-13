package com.marotech.skillhub.action.site;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@SkipAuthentication
@UrlBinding("/web/help")
public class HelpActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(JSP);
    }

    public String getNavSection() {
        return "home";
    }

    private static final String JSP = "/WEB-INF/jsp/site/help.jsp";
}
