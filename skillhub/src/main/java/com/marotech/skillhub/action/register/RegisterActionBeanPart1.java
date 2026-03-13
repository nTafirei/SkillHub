package com.marotech.skillhub.action.register;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
import com.marotech.skillhub.action.converters.EnumConverter;
import com.marotech.skillhub.util.Constants;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;

@SkipAuthentication
@UrlBinding("/web/register1")
public class RegisterActionBeanPart1 extends UserBaseActionBean {

    @Getter
    @Setter
    @Validate(required = true, converter = EnumConverter.class)
    private RegType regType;
    @DefaultHandler
    public Resolution view() {
        getContext().getRequest().getSession()
                .setAttribute(Constants.LOGGED_IN_USER, null);
        return new ForwardResolution(REGISTER1_JSP);
    }

    @HandlesEvent(SAVE)
    public Resolution save() {
        return new RedirectResolution(NEXT + "?regType=" + regType);
    }

    @Override
    public String getNavSection() {
        return "register";
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors)
            throws Exception {
        super.handleValidationErrors(errors);
        return new ForwardResolution(getErrorPage());
    }

    public RegType[] getRegTypes(){
        return RegType.values();
    }

    @Override
    protected String getErrorPage() {
        return REGISTER1_JSP;
    }

    private static final String REGISTER1_JSP = "/WEB-INF/jsp/user/register/register1.jsp";
    private static final String NEXT = "/web/register2";
}
