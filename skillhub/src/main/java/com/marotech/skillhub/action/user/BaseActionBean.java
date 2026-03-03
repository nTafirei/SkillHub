package com.marotech.skillhub.action.user;


import com.marotech.skillhub.action.HubActionBeanContext;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.model.User;
import com.marotech.skillhub.components.security.SecurityAwareActionBean;
import com.marotech.skillhub.util.Constants;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseActionBean extends SecurityAwareActionBean implements
        ValidationErrorHandler {

    protected Logger log = LoggerFactory.getLogger(getClass());
    protected static final String DELETE = "delete";
    protected static final String DISABLE = "disable";
    protected static final String REMOVE = "remove";
    protected static final String ADD = "add";
    protected static final String SAVE = "save";
    protected static final String EDIT = "edit";
    protected static final String SEARCH = "search";
    protected static final String DEPLOYMENT_ENV = "env.deployment";
    private Map<String, String> navSectionClasses = new HashMap<String, String>();
    protected HubActionBeanContext context;

    public String getNavSection() {
        return "";
    }

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (HubActionBeanContext) context;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> printNormalizedRequestParameters() { // removes
        // duplicates
        // from
        // values
        // array

        LOG.debug("-----------------START REQUEST PARAMS-----------------------");

        Map<String, String[]> requestParameters = context.getRequest()
                .getParameterMap();
        Map<String, String> map = new HashMap<String, String>();

        for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
            String name = entry.getKey().trim();
            String[] values = getContext().getRequest()
                    .getParameterValues(name);

            if (values != null) {
                for (String val : values) {
                    map.put(name, val);
                    LOG.debug(name + " = " + val);
                }
            }
        }

        LOG.debug("-----------------END REQUEST PARAMS-----------------------");
        return map;
    }


    public User getCurrentUser() {
        User user = (User) getContext().getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER);
        if (user == null) {
            return null;
        }
        return user;
    }

    public User getLoggedInUser() {
        return (User) getContext().getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER);
    }

    protected String getDeploymentEnv() {
        return config.getProperty(DEPLOYMENT_ENV);
    }

    public boolean getIsLoggedUser() {
        return getCurrentUser() != null;
    }

    public String getBaseUrl() {
        return config.getProperty(getDeploymentEnv() + ".servername")
                + config.getProperty("context.path");
    }

    public String getSecureBaseUrl() {
        return config.getProperty(getDeploymentEnv() + ".secure.servername")
                + config.getProperty("context.path");
    }

    public boolean isAnnotationPresent(Class clazz) {
        return getClass().isAnnotationPresent(clazz);
    }

    public Map<String, String> getNavSectionClasses() {
        return navSectionClasses;
    }

    public boolean getIsLoggedIn() {
        return getContext().getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER) != null;
    }

    public int getYear() {
        LocalDate date = LocalDate.now();
        return date.getYear();
    }

    public void setContext(HubActionBeanContext context) {
        this.context = context;
    }

    public String getLetter() {
        return "All";
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        if (errors.size() > 0) {
            log.error("Please override handleValidationErrors() in your current action bean");
            log.error("To see the on the current page, please override getErrorPage() with the name of the current page and remember to call this tag <stripes:errors/>");
            Set<String> keys = errors.keySet();
            for (String key : keys) {
                log.error("Validation Error Key : "
                        + errors.get(key).get(0).getFieldName());
                log.error("Params are : " + printNormalizedRequestParameters());
            }
        }
        return new ForwardResolution(getErrorPage());
    }

    protected String getErrorPage() {
        return "/WEB-INF/jsp/error.jsp";
    }

    protected abstract Resolution getHomePage();

    public void alert(Object obj) {
        log.debug("" + obj);
    }

    public String getImagePath() {
        return getContext().getRequest().getContextPath() + getImagesDir();
    }

    public String getImageFolder() {
        return "images";
    }

    public String getServerPath() {
        return config.getProperty(getDeploymentEnv() + ".servername")
                + config.getProperty("context.path");
    }

    public String getImagesDir() {
        String dir = config.getProperty("app.images.dir");
        if (dir == null) {
            return "/images";
        }
        return dir;
    }

    public String getScriptDir() {
        String dir = config.getProperty("app.script.dir");
        if (dir == null) {
            return "/script";
        }
        return dir;
    }

    public String getCssDir() {
        String dir = config.getProperty("app.css.dir");
        if (dir == null) {
            return "/css";
        }
        return dir;
    }

    public String getCssPath() {
        return getContext().getRequest().getContextPath() + getCssDir();
    }

    public String getScriptPath() {
        return getContext().getRequest().getContextPath() + getScriptDir();
    }

    protected static final String AGENT = "Agent";
    protected static final String AGENT_SUPERVISOR = "Agent Supervisor";
    @SpringBean
    protected Config config;

    protected String target;

    public boolean showRoles(){
        return config.getBooleanProperty("show.roles.as.debug");
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Config getConfig() {
        return config;
    }

    private static final Logger LOG = LoggerFactory.getLogger(BaseActionBean.class);
}
