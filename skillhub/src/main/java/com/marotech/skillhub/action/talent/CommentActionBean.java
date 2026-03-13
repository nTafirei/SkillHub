package com.marotech.skillhub.action.talent;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.CommentConverter;
import com.marotech.skillhub.action.converters.UserConverter;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.util.Constants;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

@SkipAuthentication
@UrlBinding("/web/comment")
public class CommentActionBean extends UserBaseActionBean {
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromFirstName;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromLastName;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromEmail;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String fromMobile;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String subject;
    @Getter
    @Setter
    @Validate(required = true, on = SAVE)
    private String body;
    @Getter
    @Setter
    @Validate(on = SAVE, required = true, converter = UserConverter.class)
    private User recipient;

    @Getter
    @Setter
    @Validate(converter = CommentConverter.class)
    private Comment parentNode;

    @DefaultHandler
    public Resolution view() throws Exception {
        if (parentNode != null) {
            subject = "RE: " + parentNode.getTitle();
        }
        return new ForwardResolution(COMPOSE_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {

        User currentUser = getCurrentUser();

        if (currentUser == null) {
            currentUser = repositoryService.findUserByMobilePhone(fromMobile);
        }

        if (currentUser == null) {
            AuthUser authUser = new AuthUser();
            fromMobile = fromMobile.replaceAll(" ", "");
            authUser.setUserName(fromMobile.toLowerCase());
            String newPassword = AuthUser.encodedPassword("change-me");
            authUser.setPassword(newPassword);
            repositoryService.save(authUser);

            String country = config.getProperty("country");

            City city = repositoryService.fetchCityByName("-", country);
            if (city == null) {
                city = new City();
                city.setName("-");
                city.setCountry(country);
                repositoryService.save(city);
            }
            Suburb suburb = repositoryService.fetchSuburbByName(city, "-");
            if (suburb == null) {
                suburb = new Suburb();
                suburb.setCity(city);
                suburb.setName("-");
                repositoryService.save(suburb);
            }

            Address theAddress = new Address();
            theAddress.setSuburb(suburb);
            theAddress.setAddress("-");
            repositoryService.save(theAddress);

            currentUser = new User(fromFirstName,
                    "-", fromLastName, theAddress,
                    fromMobile, "-");
            authUser.setUser(currentUser);
            System.out.println(theAddress);
            UserRole role = repositoryService.findUserRoleByRoleName(Constants.USER);
            currentUser.getUserRoles().add(role);

            repositoryService.save(currentUser);
            repositoryService.save(authUser);
        }

        Notification notification = new Notification();
        notification.setBody(body);
        if(parentNode == null){
            notification.setSubject("A comment response: " + subject);
        }
        else {
            notification.setSubject("Your new review: " + subject);
        }
        notification.setSender(currentUser);
        notification.setRecipient(recipient);
        repositoryService.save(notification);

        Comment comment = new Comment();
        comment.setTalent(recipient);
        comment.setCreatedBy(currentUser);
        comment.setTitle(subject);
        comment.setBody(body);
        comment.setParentNode(parentNode);
        repositoryService.save(comment);

        template = context.createProducerTemplate();
        template.sendBody(config.getProperty("sms.endpoint.url"), notification);
        template.sendBody(config.getProperty("email.endpoint.url"), notification);
        return new RedirectResolution(NEXT_PAGE);
    }

    @Override
    public String getNavSection() {
        return "talent";
    }

    @Override
    protected String getErrorPage() {
        return COMPOSE_JSP;
    }

    @SpringBean
    private RepositoryService repositoryService;
    @SpringBean
    private CamelContext context;
    private ProducerTemplate template;
    public static final String NEXT_PAGE = "/web/sent";
    public static final String COMPOSE_JSP = "/WEB-INF/jsp/user/talent/comment.jsp";
}
