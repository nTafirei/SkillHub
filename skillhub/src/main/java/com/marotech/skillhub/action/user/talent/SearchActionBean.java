package com.marotech.skillhub.action.user.talent;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.CategoryConverter;
import com.marotech.skillhub.action.user.converters.SkillConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.Skill;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.Comparator;
import java.util.List;

@SkipAuthentication
@UrlBinding("/web/search-for-talent")
public class SearchActionBean extends UserBaseActionBean {

    @Getter
    private List<User> users;

    @Getter
    private List<Category> categories;
    @Getter
    private List<Skill> skills;
    @Getter
    @Setter
    @Validate(on = SEARCH, converter = CategoryConverter.class)
    private Category category;
    @Getter
    @Setter
    @Validate(on = SEARCH, converter = SkillConverter.class)
    private Skill skill;

    @DefaultHandler
    public Resolution view() {
        categories = repositoryService.fetchAllCategories();
        categories.sort(Comparator.comparing(Category::getName));
        return new ForwardResolution(SEARCH_JSP);
    }

    @HandlesEvent(SEARCH)
    public Resolution search() throws Exception {

        categories = repositoryService.fetchAllCategories();

        // Validate category selection
        if (category == null) {
            getContext().getValidationErrors().add("category",
                    new SimpleError("Please select a category first."));
            categories.sort(Comparator.comparing(Category::getName));
            return new ForwardResolution(SEARCH_JSP);
        }
        categories.sort(Comparator.comparing(Category::getName));

        // Fetch and populate skills for selected category
        skills = repositoryService.fetchSkillsForCategory(category);

        if (skills == null || skills.isEmpty()) {
            getContext().getValidationErrors().add("skills",
                    new SimpleError("No skills found for selected category."));
            return new ForwardResolution(SEARCH_JSP);
        }
        skills.sort(Comparator.comparing(Skill::getName));

        // Validate skill selection
        if (skill == null) {
            getContext().getValidationErrors().add("skill",
                    new SimpleError("Please select a skill."));
            return new ForwardResolution(SEARCH_JSP);
        }

        // Fetch users with the selected skill
        users = repositoryService.fetchUsersWithSkill(skill);
        if (users == null || users.isEmpty()) {
            getContext().getValidationErrors().add("talent",
                    new SimpleError("No users found with the selected skill."));
        }
        users.sort(Comparator.comparing(User::getLastName));
        return new ForwardResolution(USERS_LIST_JSP);
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
        return "talent";
    }

    public static Comparator<Category> sortByName() {
        return Comparator.comparing(Category::getName);
    }

    public int getUsersSize() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String SEARCH_JSP = "/WEB-INF/jsp/user/talent/search.jsp";
    private static final String USER_DETAILS = "/web/user-details";
    private static final String USERS_LIST_JSP = "/WEB-INF/jsp/user/talent/list.jsp";

}
