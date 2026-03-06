package com.marotech.skillhub.action.user.talent;

import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.CategoryConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.User;
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


@UrlBinding("/web/articles/{_eventName}")
public class ArticlesActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    private int currPage = 0;
    @Getter
    @Setter
    @Validate(converter = CategoryConverter.class)
    private Category category;
    @Getter
    private Iterable<User> talent = new ArrayList<>();

    @DefaultHandler
    public Resolution list() {
        int perPage = config.getIntegerProperty("app.items.per.page");
        int start = currPage * perPage;
        if (currPage == 0) {
            start = 1;
        }
        return new ForwardResolution(LIST_JSP);
    }

    public long getUsersSize() {
        return talent.spliterator().getExactSizeIfKnown();
    }

    @Override
    protected String getErrorPage() {
        return LIST_JSP;
    }
    public List<Category> getCategories() {
        return repositoryService.fetchAllCategories();
    }
    @Override
    public String getNavSection() {
        return "talent";
    }

    @SpringBean
    private RepositoryService repositoryService;
    private static final String LIST_JSP = "/WEB-INF/jsp/user/talent/articles.jsp";
}
