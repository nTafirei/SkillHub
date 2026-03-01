<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Tools">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                    <s:errors/>

                    <security:protected-element name="show-tools">

                        <table class="alternating">
                                <thead>
                                <tr>
                                    <td class="strong">
                                        <fmt:message key="namelabel"/>
                                    </td>
                                    <td class="strong">
                                        <fmt:message key="descriptionlabel"/>
                                    </td>
                                    <td class="strong" align="right">
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td class="strong" nowrap>
                                               <strong> <fmt:message key="chatlabel"/></strong>
                                        </td>
                                        <td>
                                                <fmt:message key="chattooldesc"/>
                                        </td>
                                        <td>
                                                   <d:link
                                                             href="/web/chat">
                                                             <fmt:message key="viewlabel"/>
                                                   </d:link>
                                        </td>
                                    </tr>
                                </tbody>
                        </table>
                    </security:protected-element>
                    <security:when-no-content-displayed>
                        <d:link href="/web/user/site/help"><fmt:message
                                key="securityexceptionlink"/></d:link>
                    </security:when-no-content-displayed>

                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
