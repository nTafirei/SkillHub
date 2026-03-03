package com.marotech.skillhub.components.session;

import com.marotech.skillhub.components.service.RepositoryService;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SessionListener implements HttpSessionListener {
    private RepositoryService repositoryService;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String id = se.getSession().getId();
        ensureRepository(se);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String id = se.getSession().getId();
    }
    private void ensureRepository(HttpSessionEvent se) {
        if(repositoryService == null) {
            ApplicationContext ctx =
                    WebApplicationContextUtils.
                            getWebApplicationContext(se.getSession().getServletContext());
            repositoryService =
                    (RepositoryService) ctx.getBean("repositoryService");
        }
    }
}
