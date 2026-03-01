package com.marotech.skillhub.action.user.workers;

import com.marotech.skillhub.action.user.converters.WorkerConverter;
import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.Worker;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/worker-details/{worker}")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class WorkerDetailsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = WorkerConverter.class, required = true)
    private Worker worker;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(AUTHOR_DETAIL_JSP);
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

    public static final String AUTHOR_DETAIL_JSP = "/WEB-INF/jsp/user/workers/worker-details.jsp";
}