package com.marotech.skillhub.components.security;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * JSP tag to secure part of a JSP file. The body is shown (or not) based on
 * whether performing an event on a supplied action bean is allowed. This
 * secures any event on any action bean, while leaving the security decisions to
 * the security manager.
 *
 * @version $Id:$
 * @worker <a href="mailto:kindop@xs4all.nl">Oscar Westra van Holthe - Kind</a>
 */
public class ProtectedElementTag extends TagSupport {
    /**
     * Version number for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Logger for this class.
     */
    /**
     * The name of the bean that is the action bean to secure. If null, the
     * current action bean of the page is used.
     */
    private String name;

    private String contentType;

    private String contentId;
    private Class<? extends J2EESecurityManager> clazz;
    private J2EESecurityManager securityManager;

    /**
     * Create the secure tag.
     */
    public ProtectedElementTag() {
        initValues();
    }

    /**
     * Release the state of this tag.
     */
    @Override
    public void release() {
        super.release();
        initValues();
    }

    /**
     * Initialize the values to (re)use this tag.
     */
    private void initValues() {
        name = null;
        contentType = null;
        contentId = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * Determine if the body should be evaluated or not.
     *
     * @return EVAL_BODY_INCLUDE if the body should be included, or SKIP_BODY
     * @throws JspException when the tag cannot (decide if to) write the body content
     */
    @Override
    public int doStartTag() throws JspException {

        // Retrieve the action bean and event handler to secure.

        SecurityAwareActionBean actionBean = (SecurityAwareActionBean) pageContext
                .findAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);

        if (name == null) {
            return EVAL_BODY_AGAIN;
        }

        ApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(actionBean.getContext()
                        .getServletContext());

        if (securityManager == null) {
            securityManager = (J2EESecurityManager) context
                    .getBean("securityManager");
        }

        if (securityManager == null) {
            if (clazz == null) {
                clazz = J2EESecurityManager.class;
            }
            try {
                securityManager = clazz.newInstance();
            } catch (InstantiationException e) {
                String msg = "Failed to configure the SecurityManager: instantiation failed. (InstantiationException)";
                throw new StripesRuntimeException(msg, e);
            } catch (IllegalAccessException e) {
                String msg = "Failed to configure the SecurityManager: instantiation failed. (IllegalAccessException)";
                throw new StripesRuntimeException(msg, e);
            }
        }

        boolean haveSecurityManager = securityManager != null;
        boolean eventAllowed;
        if (haveSecurityManager) {
            eventAllowed = Boolean.TRUE.equals(securityManager
                    .getAccessAllowed(actionBean, this));
        } else {
            eventAllowed = true;
        }

        if (eventAllowed) {
            actionBean.setAtLeastOneSecuredTagRendered(true);
        }

        if (eventAllowed) {
            actionBean.incrementMenuItemCount();
        }
        // Show the tag's content (or not) based on this
        return eventAllowed ? EVAL_BODY_AGAIN : SKIP_BODY;
    }
}
