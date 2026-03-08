package com.marotech.skillhub.action.user.outbox;

import com.marotech.skillhub.action.user.SkipAuthentication;
import com.marotech.skillhub.action.user.UserBaseActionBean;
import com.marotech.skillhub.util.Constants;
import net.sourceforge.stripes.action.*;

@SkipAuthentication
@UrlBinding("/web/sent")
public class SentActionBean extends UserBaseActionBean {

    @DefaultHandler
    public Resolution sent() throws Exception {
        return new ForwardResolution(SENT_JSP);
    }

    public static final String SENT_JSP = "/WEB-INF/jsp/user/outbox/sent.jsp";
}
