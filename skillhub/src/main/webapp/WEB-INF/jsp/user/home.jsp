<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Home">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper" style="background-color: #fdfdfd; border-radius: 10px 10px 10px 10px;overflow:hidden;">
            <div class="container">
                <div id="content">
                    <strong>What is the SkillHub platform?</strong><br/>
                    SkillHub allows you to find qualified trades people near your area.
                    <br/>
                </div>
            </div>
        </div>
        <div>
            <s:errors/>
        </div>
    </s:layout-component>
</s:layout-render>

