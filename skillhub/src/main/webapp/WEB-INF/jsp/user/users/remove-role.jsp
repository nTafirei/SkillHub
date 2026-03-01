<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Remove Role">

    <s:layout-component name="head">
     </s:layout-component>

    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <security:protected-element name="manage-roles">
                        <table class="alternating">
                            <thead>
                                <tr>
                                    <th colspan="2">
                                        Staff Details
                                    </th>
                                </tr>
                            </thead>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="firstnamelabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.firstName}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="lastnamelabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.lastName}
                                </td>
                            </tr>
                            <tr>
                                 <td class="strong">
                                    <fmt:message key="roleslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.user.userRoles}" var="role"
                                                                                varStatus="loopStatus">
                                        <d:link href="/web/remove-role/${actionBean.user.id}/${role.id}/demote">
                                          <fmt:message key="removelabel"/> ${role.roleName}</d:link>
                                        <br/>
                                     </c:forEach>
                                 </td>
                             </tr>
                            </tbody>
                        </table>
                        </security:protected-element>
                     <security:when-no-content-displayed>
                        <fmt:message key="securityexceptionlink"/>
                     </security:when-no-content-displayed>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
