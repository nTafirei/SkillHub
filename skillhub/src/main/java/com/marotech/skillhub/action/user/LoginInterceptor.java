package com.marotech.skillhub.action.user;

import com.marotech.skillhub.model.User;
import com.marotech.skillhub.util.Constants;
import com.marotech.skillhub.util.HubActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({LifecycleStage.CustomValidation})
public class LoginInterceptor implements Interceptor, ConfigurableComponent {

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

        BaseActionBean baseActionBean = (BaseActionBean) executionContext.getActionBean();
        if (baseActionBean.isAnnotationPresent(SkipAuthentication.class)) {
            return executionContext.proceed();
        }
        String target = ((HubActionBeanContext) executionContext
                .getActionBean().getContext()).getTarget();

        User user = (User) executionContext.getActionBean().getContext()
                .getRequest().getSession()
                .getAttribute(Constants.LOGGED_IN_USER);

        if (user == null) {
            if (StringUtils.isBlank(target)) {
                LOG.error("Redirecting to login page");
                return new RedirectResolution(WEB_USER_LOGIN);
            } else {
                LOG.debug("Redirecting to target page: " + target);
                executionContext.getActionBean().getContext()
                        .getValidationErrors().clear();
                return new RedirectResolution(WEB_USER_HOME_VIEW_TARGET
                        + target);
            }
        }
        return executionContext.proceed();
    }

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);
    public static final String WEB_USER_LOGIN = "/web/login";
    public static final String WEB_USER_HOME_VIEW_TARGET = "/web/login/view?target=";
}
