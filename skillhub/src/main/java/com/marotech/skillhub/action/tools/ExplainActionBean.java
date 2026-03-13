package com.marotech.skillhub.action.tools;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@SkipAuthentication
@UrlBinding("/web/explain/{type}")
public class ExplainActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(on = SEARCH, required = true)
    private String type;
    @Getter
    private String messageKey;

    @DefaultHandler
    public Resolution list() {
        if ("automation".equalsIgnoreCase(type)) {
            messageKey = "automationexplained";
        } else {
            messageKey = "workflowexplained";
        }
        return new ForwardResolution(EXPLAIN_JSP);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return EXPLAIN_JSP;
    }

    @Override
    public String getNavSection() {
        if ("automation".equalsIgnoreCase(type)) {
            return "automations";
        } else {
            return "workflows";
        }
    }

    private static final String EXPLAIN_JSP = "/WEB-INF/jsp/user/tools/explain.jsp";
}
