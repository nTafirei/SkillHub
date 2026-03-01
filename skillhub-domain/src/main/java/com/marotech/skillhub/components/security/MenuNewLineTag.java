package com.marotech.skillhub.components.security;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import net.sourceforge.stripes.controller.StripesConstants;

public class MenuNewLineTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    public MenuNewLineTag() {
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

        SecurityAwareActionBean actionBean = (SecurityAwareActionBean) pageContext
                .findAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);

        int menuItemCount = actionBean.getMenuItemCount();
        boolean render = false;

        render = menuItemCount > 4;
        return render ? EVAL_BODY_AGAIN : SKIP_BODY;
    }
}
