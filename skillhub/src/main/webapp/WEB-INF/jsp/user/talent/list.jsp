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
                            <fmt:message key="notakentedsersfound"/>
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.users}">
                                        <strong>${actionBean.usersSize}
                                        <fmt:message key="takentedsersfound"/></strong>
                                    </c:if>
                                </td>
                                <td align="center" colspan="3"><d:link href="/web/search-talent"><fmt:message key="searchusersmenulabel"/></d:link></td>
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
                                        <fmt:message key="emaillabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="mobilephonelabel"/>
                                    </td>
                                     <td>
                                         <fmt:message key="id"/>
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
                                                ${user.firstName} ${user.lastName}
                                        </td>
                                        <td>
                                                <a href="mailto:${user.email}">${user.email}</a>
                                        </td>
                                        <td nowrap="nowrap">
                                                ${user.mobilePhone}
                                        </td>
                                        <td nowrap="nowrap">
                                             ${user.nationalId}
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
