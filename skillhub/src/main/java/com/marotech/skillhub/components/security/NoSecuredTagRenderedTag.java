package com.marotech.skillhub.components.security;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import net.sourceforge.stripes.controller.StripesConstants;

public class NoSecuredTagRenderedTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    public NoSecuredTagRenderedTag() {
    }

    @Override
    public void release() {
        super.release();
    }

    public SecurityAwareActionBean getActionBean() {
        return (SecurityAwareActionBean) pageContext
                .findAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
    }

    @Override
    public int doStartTag() throws JspException {

        // Retrieve the action bean and event handler to secure.

        SecurityAwareActionBean actionBean = (SecurityAwareActionBean) pageContext
                .findAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);

        boolean render = !actionBean.isAtLeastOneSecuredTagRendered();

        if (actionBean.getContext().getValidationErrors().size() > 0) {
            render = false;
        }
        return render ? EVAL_BODY_AGAIN : SKIP_BODY;
    }
}
