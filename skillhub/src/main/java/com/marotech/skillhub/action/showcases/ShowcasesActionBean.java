package com.marotech.skillhub.action.showcases;

import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.EnumConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.User;
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
    private List<User> showcasedTalent;
    @Getter
    @Setter
    private int currPage = 0;

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
            showcasedTalent = repositoryService.findShowcasedTalentByCategory(category, start, perPage);
        }else{
            showcasedTalent = repositoryService.findShowcasedTalent(start, perPage);
        }
    }

    public long getShowcasedTalentSize() {
        if (showcasedTalent == null) {
            return 0;
        }
        return showcasedTalent.size();
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
