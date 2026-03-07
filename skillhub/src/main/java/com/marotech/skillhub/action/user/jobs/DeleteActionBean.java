package com.marotech.skillhub.action.user.jobs;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.JobConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Job;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

@UrlBinding("/web/delete-job")
@RequiresOneRoleOf({"Customer Service", "Administrator", "System Administrator"})
public class DeleteActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(required = true, converter = JobConverter.class)
    protected Job job;

    @DefaultHandler
    public Resolution delete() throws Exception {
        repositoryService.getRepository().delete(job);
        return new RedirectResolution(NEXT_PAGE);
    }
    @Override
    protected String getErrorPage() {
        return JSP;
    }
    @Override
    public String getNavSection() {
        return "jobs";
    }

    @SpringBean
    protected RepositoryService repositoryService;
    public static final String NEXT_PAGE = "/web/jobs/";
    public static final String JSP = "/WEB-INF/jsp/user/jobs/jobs.jsp";
}
