package com.marotech.skillhub.action;

import com.marotech.skillhub.model.User;
import com.marotech.skillhub.util.Constants;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Intercepts({LifecycleStage.CustomValidation})
public class RoleInterceptor implements Interceptor, ConfigurableComponent {

    public void init(Configuration configuration)
            throws StripesRuntimeException {
    }

    public Resolution intercept(ExecutionContext executionContext)
            throws Exception {
        Resolution resolution;

        switch (executionContext.getLifecycleStage()) {
            case CustomValidation:
                resolution = interceptCustomValidation(executionContext);
                break;
            default:
                resolution = executionContext.proceed();
                break;
        }
        return resolution;
    }

    protected Resolution interceptCustomValidation(
            ExecutionContext executionContext) throws Exception {

        if (!executionContext.getActionBean().getClass()
                .isAnnotationPresent(RequiresOneRoleOf.class)) {
            return executionContext.proceed();
        }
        if (!executionContext.getHandler()
                .isAnnotationPresent(RequiresOneRoleOf.class)) {
            return executionContext.proceed();
        }

        User user = (User) executionContext.getActionBean().getContext()
                .getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER);

        if (user == null) {
            LOG.error("No user was found in the session for role validation");
            return new RedirectResolution(WEB_USER_LOGIN);
        } else {
            String[] roleNames = executionContext.getActionBean().
                    getClass().getAnnotation(RequiresOneRoleOf.class).value();

            boolean show = ((BaseActionBean)executionContext.getActionBean()).showRoles();

            if(show){
                LOG.debug("RoleInterceptorRoles found: " +
                        user.getRoleNames());
                LOG.debug("RoleInterceptor: Class : " + executionContext.getActionBean().
                        getClass().getName() + " requires these roles: " +
                        Arrays.asList(roleNames));
            }

            if(roleNames != null && roleNames.length > 0) {
                if (!user.hasOneRoleOf(roleNames)) {

                    LOG.debug("User has these roles : " + user.getRoleNames());

                    executionContext.getActionBean().getContext()
                            .getValidationErrors().clear();
                    executionContext.getActionBean().getContext()
                            .getRequest().getSession()
                            .setAttribute(Constants.LOGGED_IN_USER, null);

                    String message = executionContext.getActionBean().
                            getClass().getName() + " requires these roles: " +
                            Arrays.asList(roleNames) + ". You have these roles : " + user.getRoleNames();

                    executionContext.getActionBean().getContext()
                            .getRequest().getSession()
                            .setAttribute(Constants.ROLE_ERROR_MESSAGE, message);

                    LOG.error("None of required roles : " +
                            Arrays.asList(roleNames) + " were found in the user profile");
                    return new RedirectResolution(WEB_USER_LOGIN);
                }
            }
        }
        return executionContext.proceed();
    }

    public static final String WEB_USER_LOGIN = "/web/login";
    private static final Logger LOG = LoggerFactory.getLogger(RoleInterceptor.class);
}
