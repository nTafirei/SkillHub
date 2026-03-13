package com.marotech.skillhub.components.security;

import com.marotech.skillhub.action.BaseActionBean;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.model.UserRole;
import jakarta.servlet.jsp.JspException;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.DontAutoLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

        String tagName = tag.getName();
        Map<String, ProtectedElement> elementSet = new HashMap<String, ProtectedElement>();
        List<ProtectedElement> elements = elementParser.getElements();

        for (ProtectedElement element : elements) {
            elementSet.put(element.getName(), element);
        }

        ProtectedElement element = elementSet.get(tagName);
        if (element.isAllowGuest()) {
            return true;
        }

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


        if (element == null) {
            for (ProtectedElement e : elements) {
                LOG.debug(e.toString());
            }
            return false;
        }

        if (!element.hasRoles() || user == null) {
            return false;
        }

        boolean show = ((BaseActionBean) bean).showRoles();

        if (show) {
            LOG.debug("J2EESecurityManager: Roles found: " +
                    user.getRoleNames());
            LOG.debug("J2EESecurityManager: Tag : " + element.getName()
                    + " requires these roles: " +
                    Arrays.asList(element.getRoles()));
        }

        for (String role : element.getRoles()) {
            if (hasRole(user, role)) {
                userHasRole = true;
                break;
            }
        }
        return userHasRole;
    }

    protected Boolean isUserAuthenticated(ActionBean bean) {
        BaseActionBean baseActionBean = (BaseActionBean) bean;
        return baseActionBean.getIsLoggedIn();
    }

    protected User getUserAuthenticated(ActionBean bean) {
        BaseActionBean baseActionBean = (BaseActionBean) bean;
        return baseActionBean.getCurrentUser();
    }

    protected Boolean hasRole(User user, String role) {
        for (UserRole userRole : user.getUserRoles()) {
            if (role.equalsIgnoreCase(userRole.getRoleName())) {
                return true;
            }
        }
        return false;
    }

    private static final Logger LOG = LoggerFactory.getLogger(J2EESecurityManager.class);
}
