package com.marotech.skillhub.components.security;

import net.sourceforge.stripes.action.ActionBean;

public abstract class SecurityAwareActionBean implements ActionBean {

    protected boolean atLeastOneSecuredTagRendered;
    protected int menuItemCount;

    public boolean isAtLeastOneSecuredTagRendered() {
        return atLeastOneSecuredTagRendered;
    }

    public boolean getAtLeastOneSecuredTagRendered() {
        return atLeastOneSecuredTagRendered;
    }

    public void setAtLeastOneSecuredTagRendered(
            boolean atLeastOneSecuredTagRendered) {
        this.atLeastOneSecuredTagRendered = atLeastOneSecuredTagRendered;
    }

    protected void incrementMenuItemCount() {
        menuItemCount++;
    }

    protected int getMenuItemCount() {
        return menuItemCount;
    }
}
