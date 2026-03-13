package com.marotech.skillhub.action.jobs;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.JobConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Job;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
@SkipAuthentication
@UrlBinding("/web/job-details")
public class JobDetailsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(required = true, converter = JobConverter.class)
    protected Job job;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(DETAILS_JSP);
    }

    @Override
    protected String getErrorPage() {
        return DETAILS_JSP;
    }
    @Override
    public String getNavSection() {
        return "jobs";
    }

    @SpringBean
    protected RepositoryService repositoryService;
    private static final String DETAILS_JSP = "/WEB-INF/jsp/user/jobs/job-details.jsp";
}
