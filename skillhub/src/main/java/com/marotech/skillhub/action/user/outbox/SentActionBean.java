package com.marotech.skillhub.action.user.outbox;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@SkipAuthentication
@UrlBinding("/web/sent")
public class SentActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution sent() throws Exception {
        return new ForwardResolution(SENT_JSP);
    }

    public static final String SENT_JSP = "/WEB-INF/jsp/user/outbox/sent.jsp";
}
