package com.marotech.skillhub.action.user.workers;

import com.marotech.skillhub.action.user.converters.WorkerConverter;
import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.Worker;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import java.util.ArrayList;


@UrlBinding("/web/workers/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class WorkersActionBean extends UserBaseActionBean {

    @Getter
    private Iterable<Worker> workers = new ArrayList<>();
    @Getter
    @Setter
    @Validate(on = DISABLE, converter = WorkerConverter.class, required = true)
    private Worker user;

    @DefaultHandler
    public Resolution list() {
        workers = repositoryService.findAllWorkers();
        return new ForwardResolution(AUTHORS_LIST_JSP);
    }

    public long getWorkersSize() {
        return workers.spliterator().getExactSizeIfKnown();
    }

    @Override
    protected String getErrorPage() {
        return AUTHORS_LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "workers";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String AUTHORS_LIST_JSP = "/WEB-INF/jsp/user/workers/list.jsp";
}
