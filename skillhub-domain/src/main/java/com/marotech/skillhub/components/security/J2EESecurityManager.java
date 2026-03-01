package com.marotech.skillhub.components.security;

import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.UserRole;
import com.marotech.skillhub.util.Constants;
import com.marotech.skillhub.components.service.RepositoryService;
import jakarta.servlet.jsp.JspException;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.DontAutoLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DontAutoLoad
@Component("securityManager")
public class J2EESecurityManager {

    @Autowired
    private ProtectedElementParser elementParser;

    @Autowired
    private FeatureValidator featureValidator;

    @Autowired
    private RepositoryService repositoryService;

    private boolean reloadUser;

    public boolean isReloadUser() {
        return reloadUser;
    }

    public void setReloadUser(boolean reloadUser) {
        this.reloadUser = reloadUser;
    }

    /**
     * This validation is session based. So if the user's roles are changed by
     * an administrator, they have to log out and log in again. Could we fetch
     * them every time and delegate caching to Hibernate? Perhaps
     */
    public Boolean getAccessAllowed(ActionBean bean, ProtectedElementTag tag)
            throws JspException {

        if (!isUserAuthenticated(bean)) {
            return false;
        }

        if (tag.getName() == null) {
            return false;
        }

        User user = getUserAuthenticated(bean);

        if (reloadUser) {
            user = (User) repositoryService.getRepository().findById(user.getId()).get();
        }

        String tagName = tag.getName();

        if (!featureValidator.isValidFeature(tagName)) {
            throw new JspException(
                    "Security/Feature error : Tag names and feature names must match. Tag name "
                            + tagName
                            + " is used in the jsp (" + bean.getContext().getSourcePage() + ") " +
                            "but is not listed as a valid feature. " +
                            "Please make sure that the tag name "
                            + tagName + " is defined in "
                            + featureValidator.getFileName());
        }

        boolean userHasRole = false;

        Map<String, ProtectedElement> elementSet = new HashMap<String, ProtectedElement>();

        List<ProtectedElement> elements = elementParser.getElements();

        for (ProtectedElement element : elements) {
            elementSet.put(element.getName(), element);
        }

        ProtectedElement element = elementSet.get(tagName);
        if (element == null) {
            return false;
        }

        if (!element.hasRoles() || user == null) {
            return false;
        }

        for (String role : element.getRoles()) {
            if (hasRole(user, role)) {
                userHasRole = true;
                break;
            }
        }
        return userHasRole;
    }

    protected Boolean isUserAuthenticated(ActionBean bean, Method handler) {
        return bean.getContext().getRequest().getUserPrincipal() != null;
    }

    protected Boolean isUserAuthenticated(ActionBean bean) {
        return bean.getContext().getRequest().getUserPrincipal() != null
                || bean.getContext().getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER) != null;
    }

    protected User getUserAuthenticated(ActionBean bean) {
        return (User) bean.getContext().getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER);
    }

    protected Boolean hasRole(ActionBean bean, Method handler, String role) {
        return bean.getContext().getRequest().isUserInRole(role);
    }

    protected Boolean hasRole(User user, String role) {
        for (UserRole userRole : user.getUserRoles()) {
            if (role.equalsIgnoreCase(userRole.getRoleName())) {
                return true;
            }
        }
        return false;
    }
}
