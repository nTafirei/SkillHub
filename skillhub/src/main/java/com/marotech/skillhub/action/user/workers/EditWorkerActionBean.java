package com.marotech.skillhub.action.user.workers;

import com.marotech.skillhub.action.user.converters.WorkerConverter;
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

@UrlBinding("/web/edit-worker/{worker}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class EditWorkerActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = WorkerConverter.class, required = true)
    private Worker worker;
    @Getter
    @Setter
    @Validate
    private String profile;
    @Getter
    @Setter
    @Validate
    private String firstName;
    @Getter
    @Setter
    @Validate
    private String lastName;
    @Getter
    @Setter
    @Validate
    private String email;

    @DefaultHandler
    public Resolution view() {
        profile = worker.getProfile();
        firstName = worker.getFirstName();
        lastName = worker.getLastName();
        email = worker.getEmail();
        return new ForwardResolution(AUTHOR_DETAIL_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() {
        worker.setProfile(profile);
        worker.setFirstName(firstName);
        worker.setLastName(lastName);
        worker.setEmail(email);
        repositoryService.save(worker);
        return new RedirectResolution("/web/workers");
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return AUTHOR_DETAIL_JSP;
    }

    @Override
    public String getNavSection() {
        return "workers";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String AUTHOR_DETAIL_JSP = "/WEB-INF/jsp/user/workers/edit.jsp";
}