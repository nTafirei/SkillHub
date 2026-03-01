package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.Publication;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@UrlBinding("/web/search-by-title")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class SearchByTitleActionBean extends UserBaseActionBean {

    @Getter
    private List<Publication> publications;
    @Getter
    @Setter
    @Validate(on = SEARCH)
    private String title;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(SEARCH_JSP);
    }

    @HandlesEvent(SEARCH)
    public Resolution search() throws Exception {
        if (StringUtils.isNoneBlank(title)) {
            publications = repositoryService.findPublicationByTitle(title);
        } else {
            publications = new ArrayList<>();
        }
        return new ForwardResolution(AUTHORS_LIST_JSP);
    }

    public long getPublicationsSize() {
        if (publications == null) {
            return 0;
        }
        return publications.size();
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
        return "publications";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String SEARCH_JSP = "/WEB-INF/jsp/user/pubs/search-by-title.jsp";
    private static final String AUTHORS_LIST_JSP = "/WEB-INF/jsp/user/pubs/list.jsp";

}
