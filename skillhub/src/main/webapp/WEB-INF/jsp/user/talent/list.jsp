<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Talent">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                        <c:if test="${empty actionBean.users}">
                            <fmt:message key="nouserssersfound"/>
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.users}">
                                        <strong>${actionBean.usersSize}
                                        <fmt:message key="usersfound"/></strong>
                                    </c:if>
                                </td>
                                <td align="center" colspan="3"><d:link href="/web/search-for-talent"><fmt:message key="searchtalentlabel"/></d:link></td>
                            </tr>
                        </table>
                        <table class="alternating">
                            <c:if test="${!empty actionBean.users}">
                                <thead>
                                <tr>
                                    <td>
                                        <fmt:message key="namelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="surbablabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="citylabel"/>
                                    </td>
                                     <td>
                                         <fmt:message key="skillslabel"/>
                                     </td>
                                    <td align="right">
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actionBean.users}" var="user"
                                           varStatus="loopStatus">
                                    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td>

                                            <c:if test="${!funcs:hasOneRoleOf(actionBean.currentUser, 'Customer Service,Administrator,System Administrator')}">
                                                ${user.abbrvName}
                                            </c:if>

                                            <security:protected-element name="view-deeper-user-details">
                                               ${user.firstName} ${user.lastName}
                                            </security:protected-element>
                                        </td>
                                        <td>
                                                ${user.address.suburb.name}
                                        </td>
                                        <td nowrap="nowrap">
                                                ${user.address.suburb.city.name}
                                        </td>
                                        <td nowrap="nowrap">
                                             ${user.skillsAsString}
                                        </td>
                                         <td align="right">
                                                 <d:link
                                                             href="/web/user-details/${user.id}">
                                                             <fmt:message key="detaillabel"/>
                                                   </d:link>

                                               <c:if test="${user.verified == 'NO'}">
                                                     <security:protected-element name="verify-users">
                                                           |
                                                           <d:link
                                                           href="/web/verify-user/${user.id}/verify">
                                                           <fmt:message key="verifyuserlabel"/>
                                                           </d:link>
                                                     </security:protected-element>
                                                </c:if>
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
