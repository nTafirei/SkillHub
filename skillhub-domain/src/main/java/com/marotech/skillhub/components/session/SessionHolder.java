package com.marotech.skillhub.components.session;

import com.marotech.skillhub.model.ApplicationContextProvider;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ConfigurableWebApplicationContext;


@Component
@DependsOn("applicationContextProvider")
public class SessionHolder {

    private Session session;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private ApplicationContextProvider applicationContextProvider;

    @PostConstruct
    public void getSession() {
        if (session == null) {
            try {
                synchronized (this) {
                    session = entityManagerFactory.unwrap(SessionFactory.class).getCurrentSession();
                    ConfigurableListableBeanFactory beanFactory = ((ConfigurableWebApplicationContext)
                            applicationContextProvider.getApplicationContext()).getBeanFactory();
                    beanFactory.registerSingleton("sessionFactory", session.getSessionFactory());
                }
            } catch (Exception e) {
                session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
                ConfigurableListableBeanFactory beanFactory = ((ConfigurableWebApplicationContext)
                        applicationContextProvider.getApplicationContext()).getBeanFactory();
                beanFactory.registerSingleton("sessionFactory", session.getSessionFactory());
            }
        }
    }
}
