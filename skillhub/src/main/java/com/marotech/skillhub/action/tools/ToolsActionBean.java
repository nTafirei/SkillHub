package com.marotech.skillhub.action.tools;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.ValidationErrors;

@SkipAuthentication
@UrlBinding("/web/tools")
public class ToolsActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution list() {
        return new ForwardResolution(LIST_JSP);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "tools";
    }

    private static final String LIST_JSP = "/WEB-INF/jsp/user/tools/list.jsp";
}
