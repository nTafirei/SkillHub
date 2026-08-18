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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                .isAnnotationPresent(RequiresOneRoleOf.class)
                && !executionContext.getHandler().
                isAnnotationPresent(RequiresOneRoleOf.class)) {
            return executionContext.proceed();
        }
        BaseActionBean baseActionBean = (BaseActionBean) executionContext.getActionBean();

        User user = baseActionBean.getCurrentUser();

        if (user == null) {
            LOG.error("No user was found in the session for role validation");
            return new RedirectResolution(WEB_USER_LOGIN);
        } else {

            List<String> theRoles = new ArrayList<>();
            String[] roleNames = null;
            RequiresOneRoleOf annotation = executionContext.getActionBean().
                    getClass().getAnnotation(RequiresOneRoleOf.class);
            if (annotation != null) {
                roleNames = annotation.value();
                if (roleNames != null && roleNames.length > 0) {
                    theRoles.addAll(Arrays.asList(roleNames));
                }
            }

            annotation = executionContext.getHandler().
                    getClass().getAnnotation(RequiresOneRoleOf.class);

            if (annotation != null) {
                roleNames = annotation.value();
                if (roleNames != null && roleNames.length > 0) {
                    theRoles.addAll(Arrays.asList(roleNames));
                }
            }

            if (!theRoles.isEmpty()) {
                if (!user.hasOneRoleOf(roleNames)) {
                    LOG.debug("Class : " + executionContext.getActionBean().
                            getClass().getName() + " requires these roles: " +
                            theRoles);
                    LOG.debug("User has these roles : " + user.getRoleNames());

                    executionContext.getActionBean().getContext()
                            .getValidationErrors().clear();

                    baseActionBean.setCurrentUser(null);

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
