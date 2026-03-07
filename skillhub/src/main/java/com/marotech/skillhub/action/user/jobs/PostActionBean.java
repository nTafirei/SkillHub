package com.marotech.skillhub.action.user.jobs;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.CategoryConverter;
import com.marotech.skillhub.action.user.converters.CityConverter;
import com.marotech.skillhub.action.user.converters.SuburbConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.City;
import com.marotech.skillhub.model.Job;
import com.marotech.skillhub.model.Suburb;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.Comparator;
import java.util.List;

@SkipAuthentication
@UrlBinding("/web/post")
public class PostActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String mobile;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String title;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String name;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String desc;
    @Getter
    @Setter
    @Validate(on = SAVE)
    private String email;
    @Getter
    @Setter
    @Validate(on = {POPULATE, SAVE}, required = true, converter = CategoryConverter.class)
    private Category category;
    @Getter
    @Setter
    @Validate(on = {POPULATE, SAVE}, required = true, converter = CityConverter.class)
    private City city;
    @Getter
    @Setter
    @Validate(on = {POPULATE, SAVE}, required = true, converter = SuburbConverter.class)
    private Suburb suburb;
    @Getter
    private List<Category> categories;
    @Getter
    private List<City> cities;
    @Getter
    private List<Suburb> suburbs;

    @DefaultHandler
    public Resolution view() throws Exception {
        categories = repositoryService.fetchAllCategories();
        categories.sort(Comparator.comparing(Category::getName));
        cities = repositoryService.fetchAllCities();
        cities.sort(Comparator.comparing(City::getName));
        return new ForwardResolution(JSP);
    }

    @HandlesEvent(POPULATE)
    public Resolution populate() throws Exception {
        cities = repositoryService.fetchAllCities();
        cities.sort(Comparator.comparing(City::getName));

        if (city != null) {
            suburbs = repositoryService.fetchSuburbsForCity(city);
            if (suburbs != null) {
                suburbs.sort(Comparator.comparing(Suburb::getName));
            }
        }
        return new ForwardResolution(JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {
        Job job = new Job();
        job.setName(name);
        job.setDesc(desc);
        job.setEmail(email);
        job.setMobile(mobile);
        job.setTitle(title);
        job.setCategory(category);
        job.setSuburb(suburb);
        repositoryService.save(job);
        return new RedirectResolution(NEXT_PAGE + "?job=" + job.getId());
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        super.handleValidationErrors(errors);
        // Repopulate dropdowns on validation errors
        if (categories == null) {
            categories = repositoryService.fetchAllCategories();
            categories.sort(Comparator.comparing(Category::getName));
        }
        if (cities == null) {
            cities = repositoryService.fetchAllCities();
            cities.sort(Comparator.comparing(City::getName));
        }
        if (city != null) {
            suburbs = repositoryService.fetchSuburbsForCity(city);
            suburbs.sort(Comparator.comparing(Suburb::getName));
        }
        return new ForwardResolution(getErrorPage());
    }

    @Override
    public String getNavSection() {
        return "post";
    }

    @Override
    protected String getErrorPage() {
        return JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    public static final String NEXT_PAGE = "/web/my-job-details";
    public static final String JSP = "/WEB-INF/jsp/user/jobs/post.jsp";
}
