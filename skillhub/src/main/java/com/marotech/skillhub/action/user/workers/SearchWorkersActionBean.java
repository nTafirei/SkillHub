package com.marotech.skillhub.action.user.workers;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.Worker;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.List;

@UrlBinding("/web/search-workers")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class SearchWorkersActionBean extends UserBaseActionBean {

    @Getter
    private List<Worker> workers;
    @Getter
    @Setter
    @Validate(on = SEARCH)
    private String firstName;
    @Getter
    @Setter
    @Validate(on = SEARCH)
    private String lastName;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(SEARCH_JSP);
    }

    @HandlesEvent(SEARCH)
    public Resolution search() throws Exception {
        workers = repositoryService.findWorkersByFirstOrLastName(firstName, lastName);
        return new ForwardResolution(AUTHORS_LIST_JSP);
    }

    public long getWorkersSize() {
        if (workers == null) {
            return 0;
        }
        return workers.size();
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return SEARCH_JSP;
    }

    @Override
    public String getNavSection() {
        return "workers";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String SEARCH_JSP = "/WEB-INF/jsp/user/workers/search.jsp";
    private static final String AUTHORS_LIST_JSP = "/WEB-INF/jsp/user/workers/list.jsp";

}
