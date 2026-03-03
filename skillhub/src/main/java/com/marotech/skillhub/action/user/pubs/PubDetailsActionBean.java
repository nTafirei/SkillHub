package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.PublicationConverter;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.Publication;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/pub-details/{publication}")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class PubDetailsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = PublicationConverter.class, required = true)
    private Publication publication;

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
        if(publication.getActiveStatus() == ActiveStatus.ACTIVE) {
            return "publications";
        }
        return "inactive-publications";
    }

    public static final String AUTHOR_DETAIL_JSP = "/WEB-INF/jsp/user/pubs/pub-details.jsp";
}