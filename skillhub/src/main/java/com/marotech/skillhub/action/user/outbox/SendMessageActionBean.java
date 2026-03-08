package com.marotech.skillhub.action.user.outbox;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.action.user.converters.UserConverter;
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
@UrlBinding("/web/send-message")
public class SendMessageActionBean extends UserBaseActionBean {
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

    @DefaultHandler
    public Resolution view() throws Exception {
        return new ForwardResolution(COMPOSE_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() throws Exception {

        User user = getCurrentUser();

        if (user == null) {
            user = repositoryService.findUserByMobilePhone(fromMobile);
        }

        if (user == null) {
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

            user = new User(fromFirstName,
                    "-", fromLastName, theAddress,
                    fromMobile, "-");
            authUser.setUser(user);
            System.out.println(theAddress);
            UserRole role = repositoryService.findUserRoleByRoleName(Constants.USER);
            user.getUserRoles().add(role);

            repositoryService.save(user);
            repositoryService.save(authUser);
        }

        Notification notification = new Notification();
        notification.setBody(body);
        notification.setSubject(subject);
        notification.setSender(user);
        notification.setRecipient(recipient);
        repositoryService.save(notification);

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
    public static final String SENT = "sent";

    public static final String COMPOSE_JSP = "/WEB-INF/jsp/user/outbox/compose.jsp";
}
