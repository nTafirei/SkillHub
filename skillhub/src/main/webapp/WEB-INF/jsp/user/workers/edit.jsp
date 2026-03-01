<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Edit Worker">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>
                    <security:protected-element name="edit-workers">
                        <s:form action="/web/edit-worker" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" name="_eventName" value="save"/>
                        <input type="hidden" name="worker" value="${actionBean.worker.id}"/>
                        <table class="alternating">
                                <tr>
                                    <td>
                                        <fmt:message key="firstnamelabel"/>
                                    </td>
                                    <td>
                                        <d:text name="firstName" id="firstName" style="background-color:#F0E68C" value="${actionBean.firstName}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="lastnamelabel"/>
                                    </td>
                                    <td>
                                        <d:text name="lastName" id="lastName" style="background-color:#F0E68C" value="${actionBean.lastName}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="emaillabel"/>
                                    </td>
                                    <td>
                                        <d:text name="email" id="email" style="background-color:#F0E68C" value="${actionBean.email}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="profilelabel"/>
                                    </td>
                                    <td>
                                        <d:textarea name="profile" id="profile" style="background-color:#F0E68C" value="${actionBean.profile}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                        <td align="right">
                                            <fmt:message key="submitlabel" var="submitlabel"/>
                                            <d:submit name="submit" class="small" value="${submitlabel}" id="login"/>
                                        </td>
                                    </tr>
                        </table>
                      </s:form>
                    </security:protected-element>
                    <security:when-no-content-displayed>
                        <d:link href="/web/admin/site/help"><fmt:message
                                key="securityexceptionlink"/></d:link>
                    </security:when-no-content-displayed>

                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
