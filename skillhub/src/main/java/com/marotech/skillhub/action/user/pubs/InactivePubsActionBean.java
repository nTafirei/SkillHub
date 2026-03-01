package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.converters.PublicationConverter;
import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.Publication;
import com.marotech.skillhub.components.service.RepositoryService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import java.util.ArrayList;
import java.util.List;


@UrlBinding("/web/inactive-publications/{publication}/{_eventName}")
@RequiresOneRoleOf({"Administrator", "System Administrator"})
public class InactivePubsActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    private int currPage = 0;
    @Getter
    @Setter
    @Validate(required = true, on = ACTIVATE, converter = PublicationConverter.class)
    private Publication publication;
    @Getter
    private Iterable<Publication> publications = new ArrayList<>();

    @DefaultHandler
    public Resolution list() {
        int perPage = config.getIntegerProperty("app.items.per.page");
        int start = currPage * perPage;
        if (currPage == 0) {
            start = 1;
        }
        publications = repositoryService.findPublications(start, perPage, ActiveStatus.NOT_ACTIVE);
        return new ForwardResolution(LIST_JSP);
    }

    @HandlesEvent(ACTIVATE)
    public Resolution activate() throws Exception {
        publication.setActiveStatus(ActiveStatus.ACTIVE);
        repositoryService.save(publication);
        return new RedirectResolution(INACTIVE_PUBLICATIONS);
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
        return "inactive-publications";
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String ACTIVATE = "activate";
    public static final String INACTIVE_PUBLICATIONS = "/web/inactive-publications";
    private static final String LIST_JSP = "/WEB-INF/jsp/user/pubs/inactive-list.jsp";
}
