<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Automation/Workflow Explained">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                        <s:errors/>
                        <fmt:message key="${actionBean.messageKey}"/>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
