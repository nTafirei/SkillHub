package com.marotech.skillhub.components.security;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;

import java.lang.reflect.Method;

public interface SecurityHandler {

    Resolution handleAccessDenied(ActionBean bean, Method handler);
}
