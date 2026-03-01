package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.converters.EnumConverter;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.Publication;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.List;

@UrlBinding("/web/search-by-source")
public class SearchBySourceActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = EnumConverter.class)
    private Category category;
    @Getter
    private List<Publication> publications;
    @Getter
    @Setter
    @Validate(required = true)
    private String source;
    @DefaultHandler
    public Resolution search() throws Exception {
        publications = repositoryService.findPublicationBySource(source);
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
    public List<Category> getCategories() {
        return Category.getSortedValues();
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

    public static final String SEARCH_JSP = "/WEB-INF/jsp/user/pubs/search-by-worker.jsp";
    private static final String AUTHORS_LIST_JSP = "/WEB-INF/jsp/user/pubs/list.jsp";
}
