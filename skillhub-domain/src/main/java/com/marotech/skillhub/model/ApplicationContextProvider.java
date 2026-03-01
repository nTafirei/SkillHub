package com.marotech.skillhub.model;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @worker nickk
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(
            ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }
}
