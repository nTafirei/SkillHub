package com.marotech.skillhub.action;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;

public class HubActionBeanContext extends ActionBeanContext {

    @Override
    public Resolution getSourcePageResolution() {
        String sourcePage = getSourcePage();
        if (sourcePage == null) {
            return new ForwardResolution(getRequest().getServletPath());
        }
        return super.getSourcePageResolution();
    }


    public String getTarget() {
        String target = getRequest().getRequestURI();
        String queryString = getRequest().getQueryString();

        if (StringUtils.isNotBlank(queryString)) {
            target += "?" + queryString;
        }

        // Remove application context path
        String appName = getRequest().getContextPath();
        int index = target.indexOf(appName);
        if (index != -1) {
            target = target.substring(index + appName.length());
        }

        return target;
    }
}
