<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Comment on Article">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>
                    <strong>${actionBean.article.title}<br/>
                    By: ${actionBean.article.workerNames}
                    </strong>
                    <security:protected-element name="edit-workers">
                        <s:form action="/web/comment" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" name="_eventName" value="save"/>
                        <input type="hidden" name="article" value="${actionBean.article.id}"/>
                        <c:if test="${!empty actionBean.parent}">
                            <input type="hidden" name="parent" value="${actionBean.parent.id}"/>
                         </c:if>
                        <table class="alternating">
                            <c:if test="${!empty actionBean.parent}">
                                <tr>
                                    <td colspan="3">
                                        <fmt:message key="parentcommentlabel"/>
                                    </td>
                                    <td colspan="3">
                                        <table>
                                            <tr>
                                                <td>
                                                    <fmt:message key="yourtitlelabel"/>
                                                </td>
                                                <td>
                                                    ${actionBean.parent.title}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <fmt:message key="bylabel"/>
                                                </td>
                                                <td>
                                                    ${actionBean.parent.createdBy.initials}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <fmt:message key="commentlabel"/>
                                                </td>
                                                <td>
                                                    ${actionBean.parent.body}
                                                </td>
                                            </tr>
                                        <table>
                                    </td>
                                </tr>
                            </c:if>
                                <tr>
                                    <td>
                                        <fmt:message key="titlelabel"/>
                                    </td>
                                    <td>
                                        <d:textarea name="title" id="title" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="yourcommentlabel"/>
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
