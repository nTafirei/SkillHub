package com.marotech.skillhub.action.user.showcases;

import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.EnumConverter;
import com.marotech.skillhub.action.user.converters.PublicationConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Article;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.repository.ShowcaseRepository;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import java.util.List;


@UrlBinding("/web/showcases")
public class ShowcasesActionBean extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(converter = EnumConverter.class)
    private Category category;
    @Getter
    private List<Article> publications;
    @Getter
    @Setter
    private int currPage = 0;
    @Getter
    @Setter
    @Validate(required = true, on = {REMOVE, ADD}, converter = PublicationConverter.class)
    private Article publication;


    @DefaultHandler
    public Resolution list() {

        //Pageable pageable = null;
        //Page<Showcase> page = showcaseRepository.findAll(pageable);
        //showcases = page.get().toList();
        fetchShowcases();
        return new ForwardResolution(CASES_LIST_JSP);
    }

    private void fetchShowcases() {
        int perPage = config.getIntegerProperty("app.items.per.page");
        int start = currPage * perPage;
        if (currPage == 0) {
            start = 1;
        }
        if (category != null) {
            publications = repositoryService.findShowcasedByCategory(category, start, perPage);
        }else{
            publications = repositoryService.findShowcasedPublications(start, perPage);
        }
    }

    public long getPublicationsSize() {
        if (publications == null) {
            return 0;
        }
        return publications.size();
    }

    @Override
    protected String getErrorPage() {
        return CASES_LIST_JSP;
    }

    @Override
    public String getNavSection() {
        return "showcases";
    }

    public List<Category> getCategories() {
        return repositoryService.fetchAllCategories();
    }

    @SpringBean
    private RepositoryService repositoryService;
    @SpringBean
    private ShowcaseRepository showcaseRepository;
    private static final String CASES_LIST_JSP = "/WEB-INF/jsp/user/showcases/list.jsp";
}
