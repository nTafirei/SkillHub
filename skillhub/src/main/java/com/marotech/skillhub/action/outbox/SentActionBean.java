package com.marotech.skillhub.action.outbox;

import com.marotech.skillhub.action.SkipAuthentication;
import com.marotech.skillhub.action.UserBaseActionBean;
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
