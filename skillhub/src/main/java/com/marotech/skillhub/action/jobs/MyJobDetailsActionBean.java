package com.marotech.skillhub.action.jobs;

import com.marotech.skillhub.action.SkipAuthentication;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
@SkipAuthentication
@UrlBinding("/web/my-job-details")
public class MyJobDetailsActionBean extends JobDetailsActionBean {
    @Override
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(DETAILS_JSP);
    }

    @Override
    protected String getErrorPage() {
        return DETAILS_JSP;
    }

    private static final String DETAILS_JSP = "/WEB-INF/jsp/user/jobs/my-job-details.jsp";
}
