<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Talent Reviews">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>

                                    <c:if test="${empty actionBean.comments}">
                                        <fmt:message key="noreviewsfound"/> for
                                            <c:if test="${!funcs:hasOneRoleOf(actionBean.currentUser, 'Customer Service,Administrator,System Administrator')}">
                                                ${actionBean.talent.abbrvName}
                                            </c:if>
                                            <security:protected-element name="view-deeper-user-details">
                                               ${actionBean.talent.firstName} ${actionBean.talent.lastName}
                                            </security:protected-element>
                                    </c:if>
                                     <c:if test="${!empty actionBean.comments}">
                                         <strong>${actionBean.commentsSize}
                                         <fmt:message key="reviewsfound"/> for
                                            <c:if test="${!funcs:hasOneRoleOf(actionBean.currentUser, 'Customer Service,Administrator,System Administrator')}">
                                                ${actionBean.talent.abbrvName}
                                            </c:if>
                                            <security:protected-element name="view-deeper-user-details">
                                               ${actionBean.talent.firstName} ${actionBean.talent.lastName}
                                            </security:protected-element>
                                         </strong>
                                     </c:if>
                        <table class="alternating">
                            <c:if test="${!empty actionBean.comments}">
                                <thead>
                                <tr>
                                    <td>
                                        <fmt:message key="pubdatelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="titlelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="authorlabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="bodylabel"/>
                                    </td>
                                    <td align="right">
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actionBean.comments}" var="comment"
                                           varStatus="loopStatus">
                                    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td>
                                                ${comment.formattedDateCreated}
                                        </td>
                                        <td>
                                                ${comment.title}
                                        </td>
                                        <td>
                                                ${comment.createdBy.abbrvName}
                                        </td>
                                        <td>
                                                ${comment.body}
                                        </td>
                                        <td align="right">
                                                     <d:link
                                                          href="/web/comment?recipient=${comment.talent.id}&parentNode=${comment.id}">
                                                          <fmt:message key="respondlabel"/>
                                                      </d:link>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:if>
                        </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
