package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.converters.EnumConverter;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.Publication;
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
import java.util.List;


@UrlBinding("/web/publications/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class PubsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    private int currPage = 0;
    @Getter
    @Setter
    @Validate(converter = EnumConverter.class)
    private Category category;
    @Getter
    private Iterable<Publication> publications = new ArrayList<>();

    @DefaultHandler
    public Resolution list() {
        int perPage = config.getIntegerProperty("app.items.per.page");
        int start = currPage * perPage;
        if (currPage == 0) {
            start = 1;
        }
        if (category != null) {
            publications = repositoryService.findPublicationsByCategory(category, start, perPage, ActiveStatus.ACTIVE);
        }else{
            publications = repositoryService.findPublications(start, perPage, ActiveStatus.ACTIVE);
        }
        return new ForwardResolution(LIST_JSP);
    }

    public long getPublicationsSize() {
        return publications.spliterator().getExactSizeIfKnown();
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }
    public List<Category> getCategories() {
        return Category.getSortedValues();
    }
    @Override
    public String getNavSection() {
        return "publications";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String LIST_JSP = "/WEB-INF/jsp/user/pubs/list.jsp";
}
