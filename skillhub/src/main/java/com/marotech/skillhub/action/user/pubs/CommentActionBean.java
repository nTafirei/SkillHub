package com.marotech.skillhub.action.user.pubs;

import com.marotech.skillhub.action.user.RequiresOneRoleOf;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.CommentConverter;
import com.marotech.skillhub.action.user.converters.PublicationConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.ActiveStatus;
import com.marotech.skillhub.model.Comment;
import com.marotech.skillhub.model.Publication;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/web/comment/{publication}/{parent}")
@RequiresOneRoleOf({"Administrator", "System Administrator", "User"})
public class CommentActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(required = true, converter = PublicationConverter.class)
    private Publication publication;
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
        if (parent != null) {
            title = "RE: " + parent.getTitle();
        }else{
            title = "RE: " + publication.getTitle();
        }
        return new ForwardResolution(COMMENT_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {
        Comment comment = new Comment();
        comment.setCreatedBy(getCurrentUser());
        comment.setTitle(title);
        comment.setBody(body);
        comment.setParentNote(parent);
        comment.setPublication(publication);
        repositoryService.save(comment);
        return new RedirectResolution("/web/comments/" + publication.getId());
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
        if(publication.getActiveStatus() == ActiveStatus.ACTIVE) {
            return "publications";
        }
        return "inactive-publications";
    }

    @SpringBean
    private RepositoryService repositoryService;

    public static final String COMMENT_JSP = "/WEB-INF/jsp/user/pubs/comment.jsp";
}