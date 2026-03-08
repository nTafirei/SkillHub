package com.marotech.skillhub.action.user.talent;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.Comment;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.List;

@SkipAuthentication
@UrlBinding("/web/talent-reviews")
public class TalentReviewsActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(converter = UserConverter.class, required = true)
    private User talent;

    @Getter
    private List<Comment> comments;

    @DefaultHandler
    public Resolution view() {
        comments = repositoryService.fetchReviewsForTalent(talent);
        return new ForwardResolution(COMMENTS_JSP);
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    public int getCommentsSize(){
        if(comments == null){
            return 0;
        }
        return  comments.size();
    }
    @SpringBean
    private RepositoryService repositoryService;
    @Override
    protected String getErrorPage() {
        return COMMENTS_JSP;
    }

    @Override
    public String getNavSection() {
        return "talent";
    }

    public static final String COMMENTS_JSP = "/WEB-INF/jsp/user/talent/talent-reviews.jsp";
}