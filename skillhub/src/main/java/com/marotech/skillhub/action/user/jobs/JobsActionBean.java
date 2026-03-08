package com.marotech.skillhub.action.user.jobs;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Job;
import lombok.Getter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.List;

@SkipAuthentication
@UrlBinding("/web/jobs")
public class JobsActionBean extends UserBaseActionBean {

    @Getter
    private List<Job> jobs;
    @DefaultHandler
    public Resolution view() throws Exception {
        jobs = repositoryService.fetchAllJobs();
        return new ForwardResolution(JSP);
    }
    public long getJobsSize() {
        if(jobs == null){
            return 0;
        }
        return jobs.size();
    }
    @Override
    public String getNavSection() {
        return "jobs";
    }

    @Override
    protected String getErrorPage() {
        return JSP;
    }
    @SpringBean
    protected RepositoryService repositoryService;
    public static final String JSP = "/WEB-INF/jsp/user/jobs/jobs.jsp";
}
