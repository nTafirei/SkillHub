package com.marotech.skillhub.action.user.talent;

import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.CommentConverter;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.action.user.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.Comment;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.User;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/comment/{talent}/{parent}")
public class CommentActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(required = true, converter = UserConverter.class)
    private User talent;
    @Getter
    @Setter
    @Validate(on = SAVE, converter = CommentConverter.class)
    private Comment parent;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String title;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true)
    private String body;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(COMMENT_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {
        Comment comment = new Comment();
        comment.setCreatedBy(getCurrentUser());
        comment.setTitle(title);
        comment.setBody(body);
        comment.setParentNote(parent);
        comment.setTalent(talent);
        repositoryService.save(comment);
        return new RedirectResolution("/web/comments/" + talent.getId());
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    @Override
    protected String getErrorPage() {
        return COMMENT_JSP;
    }

    @Override
    public String getNavSection() {
        return "talent";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String COMMENT_JSP = "/WEB-INF/jsp/user/talent/comment.jsp";
}