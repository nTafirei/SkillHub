package com.marotech.skillhub.action.user.talent;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.CategoryConverter;
import com.marotech.skillhub.action.user.converters.CityConverter;
import com.marotech.skillhub.action.user.converters.SkillConverter;
import com.marotech.skillhub.action.user.converters.SuburbConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.*;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SkipAuthentication
@UrlBinding("/web/search-for-talent")
public class SearchActionBean extends UserBaseActionBean {

    @Getter
    private List<User> users;

    private List<Category> categories;
    private List<Skill> skills;
    @Getter
    @Setter
    @Validate(converter = CategoryConverter.class)
    private Category category;
    @Getter
    @Setter
    @Validate(converter = SkillConverter.class)
    private Skill skill;
    @Getter
    @Setter
    @Validate(converter = CityConverter.class)
    private City city;
    @Getter
    @Setter
    @Validate(converter = SuburbConverter.class)
    private Suburb suburb;

    private List<City> cities;

    private List<Suburb> suburbs;

    @DefaultHandler
    public Resolution view() {
        categories = repositoryService.fetchAllCategories();
        categories.sort(Comparator.comparing(Category::getName));
        cities = repositoryService.fetchAllCities();
        cities.sort(Comparator.comparing(City::getName));
        return new ForwardResolution(SEARCH_JSP);
    }

    @HandlesEvent(POPULATE)
    public Resolution populate() throws Exception {
        categories = repositoryService.fetchAllCategories();
        categories.sort(Comparator.comparing(Category::getName));

        // Fetch skills if category is selected (optional)
        if (category != null) {
            skills = repositoryService.fetchSkillsForCategory(category);
            if (skills != null) {
                skills.sort(Comparator.comparing(Skill::getName));
            }
        }
        return new ForwardResolution(SEARCH_JSP);
    }

    @HandlesEvent(SEARCH)
    public Resolution search() throws Exception {

        // Single database query with all filters applied at DB level
        users = repositoryService.fetchUsersByFilters(category, skill, city, suburb);

        if (users == null || users.isEmpty()) {
            getContext().getValidationErrors().add("talent",
                    new SimpleError("No users found matching your criteria."));
            return new ForwardResolution(SEARCH_JSP);
        }

        users.sort(Comparator.comparing(User::getLastName));
        return new ForwardResolution(USERS_LIST_JSP);
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
        return new ForwardResolution(getErrorPage());
    }

    public int getUsersSize() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    public List<Skill> getSkills() {
        if (skills == null) {
            if (category != null) {
                skills = repositoryService.fetchSkillsForCategory(category);
                if (skills != null) {
                    skills.sort(Comparator.comparing(Skill::getName));
                }
            } else {
                skills = Collections.emptyList();
            }
        }
        return skills;
    }

    public List<Category> getCategories() {
        if (categories == null) {
            categories = repositoryService.fetchAllCategories();
            categories.sort(Comparator.comparing(Category::getName));
        }
        return categories;
    }

    // Getter for city population on error pages
    public List<City> getCities() {
        if (cities == null) {
            cities = repositoryService.fetchAllCities();
            cities.sort(Comparator.comparing(City::getName));
        }
        return cities;
    }

    // Getter for suburb population (if needed for cascading dropdowns)
    public List<Suburb> getSuburbs() {
        if (city != null && suburbs == null) {
            suburbs = repositoryService.fetchSuburbsForCity(city);
            if (suburbs != null) {
                suburbs.sort(Comparator.comparing(Suburb::getName));
            }
        }
        return suburbs;
    }

    @Override
    protected String getErrorPage() {
        return SEARCH_JSP;
    }

    @Override
    public String getNavSection() {
        return "talent";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String SEARCH_JSP = "/WEB-INF/jsp/user/talent/search.jsp";
    private static final String USER_DETAILS = "/web/user-details";
    private static final String USERS_LIST_JSP = "/WEB-INF/jsp/user/talent/list.jsp";
}


