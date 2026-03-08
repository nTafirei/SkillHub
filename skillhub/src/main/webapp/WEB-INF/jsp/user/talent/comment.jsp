<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Review Talent">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>
                        <s:form action="/web/comment" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" name="_eventName" value="save"/>
                        <input type="hidden" name="recipient" value="${actionBean.recipient.id}"/>

                        <c:if test="${!empty actionBean.parentNode}">
                            <input type="hidden" name="parentNode" value="${actionBean.parentNode.id}"/>
                        </c:if>

                        <c:if test="${actionBean.isLoggedIn == true}">
                            <input type="hidden" name="fromFirstName" value="${actionBean.currentUser.firstName}"/>
                            <input type="hidden" name="fromLastName" value="${actionBean.currentUser.lastName}"/>
                            <input type="hidden" name="fromEmail" value="${actionBean.currentUser.email}"/>
                            <input type="hidden" name="fromMobile" value="${actionBean.currentUser.mobilePhone}"/>
                        </c:if>

                        <table class="alternating">
                            <thead>
                                <c:if test="${empty actionBean.parentNode}">
                                    <tr>
                                        <tr colspan="2" align="center">
                                            <fmt:message key="writereviewon"/>
                                                <c:if test="${!funcs:hasOneRoleOf(actionBean.currentUser, 'Customer Service,Administrator,System Administrator')}">
                                                    ${actionBean.recipient.abbrvName}
                                                </c:if>

                                                <security:protected-element name="view-deeper-user-details">
                                                   ${actionBean.recipient.firstName} ${actionBean.recipient.lastName}
                                                </security:protected-element>
                                        </th>
                                    <tr>
                                </c:if>
                                <c:if test="${!empty actionBean.parentNode}">
                                    <tr>
                                        <tr colspan="2" align="center">
                                            <fmt:message key="respondto"/> <u>'${actionBean.parentNode.title}'</u> on
                                                <c:if test="${!funcs:hasOneRoleOf(actionBean.currentUser, 'Customer Service,Administrator,System Administrator')}">
                                                    ${actionBean.recipient.abbrvName}
                                                </c:if>

                                                <security:protected-element name="view-deeper-user-details">
                                                   ${actionBean.recipient.firstName} ${actionBean.recipient.lastName}
                                                </security:protected-element>
                                                by
                                                <c:if test="${!funcs:hasOneRoleOf(actionBean.currentUser, 'Customer Service,Administrator,System Administrator')}">
                                                    ${actionBean.parentNode.createdBy.abbrvName}
                                                </c:if>

                                                <security:protected-element name="view-deeper-user-details">
                                                   ${actionBean.parentNode.createdBy.firstName} ${actionBean.parentNode.createdBy.lastName}
                                                </security:protected-element>
                                                <br/><fmt:message key="commentbody"/>: ${actionBean.parentNode.body}

                                        </th>
                                    <tr>
                                </c:if>
                            </thead>
                            <tbody>
                            <c:if test="${actionBean.isLoggedIn == false}">
                                <tr>
                                    <td>
                                        <fmt:message key="firstnamelabel"/>
                                    </td>
                                    <td>
                                        <d:text name="fromFirstName" id="fromFirstName" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="lastnamelabel"/>
                                    </td>
                                    <td>
                                        <d:text name="fromLastName" id="fromLastName" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="emaillabel"/>
                                    </td>
                                    <td>
                                        <d:text name="fromEmail" id="fromEmail" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="mobilelabel"/>
                                    </td>
                                    <td>
                                        <d:text name="fromMobile" id="fromMobile" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                            </c:if>

                                <tr>
                                    <td>
                                        <fmt:message key="subjectlabel"/>
                                    </td>
                                    <td>
                                        <d:text name="subject" id="subject" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="bodylabel"/>
                                    </td>
                                    <td>
                                        <d:textarea name="body" id="body" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                        <td align="right">
                                            <fmt:message key="submitlabel" var="submitlabel"/>
                                            <d:submit name="submitbtn" class="small" value="${submitlabel}" id="login"/>
                                        </td>
                                    </tr>
                            </tbody>
                        </table>
                      </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
