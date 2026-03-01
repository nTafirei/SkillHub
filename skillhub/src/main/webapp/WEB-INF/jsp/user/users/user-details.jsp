<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="User Details">

    <s:layout-component name="head">
     </s:layout-component>

    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                        <table class="alternating">
                            <thead>
                                <tr>
                                    <th colspan="2">
                                        <u><fmt:message key="userdetailsheaderlabel"/></u>
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
                                    <fmt:message key="nationalidlabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.nationalId}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="addresslabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.address}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="citylabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.city}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="countrylabel"/>
                                </td>
                                <td>
                                        ${actionBean.user.country}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                   <fmt:message key="mobilephonelabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.mobilePhone}
                                </td>
                            </tr>
                            <tr>
                                 <td class="strong">
                                    <fmt:message key="roleslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.user.userRoles}" var="role"
                                                                                varStatus="loopStatus">
                                     ${role.roleName}
                                                <c:if test="${funcs:hasMore(loopStatus.index,actionBean.user.userRoles) == true}">
                                                 ,&nbsp;&nbsp;
                                                </c:if>
                                     </c:forEach>
                                 </td>
                             </tr>
                              <tr>
                                  <td class="strong">
                                     <fmt:message key="emaillabel"/>
                                  </td>
                                  <td>
                                      <a href="mailto:${actionBean.user.email}">${actionBean.user.email}</a>
                                  </td>
                              </tr>
                              <tr>
                                  <td class="strong">
                                     <fmt:message key="verified"/>
                                  </td>
                                  <td>
                                      ${actionBean.user.verified}
                                  </td>
                              </tr>
                            <tr>
    <td align="right">



                                 <security:protected-element name="disable-users">
                                                      <d:link
                                                          href="/web/disable/${actionBean.user.id}">
                                                          <fmt:message key="disablelabel"/>
                                                      </d:link>
                                 </security:protected-element>
                                 <security:protected-element name="manage-roles">
                                                      <d:link
                                                          href="/web/assign-role/${actionBean.user.id}">
                                                          <fmt:message key="assignroleslabel"/>
                                                      </d:link>
                                                     | <d:link
                                                          href="/web/remove-role/${actionBean.user.id}">
                                                          <fmt:message key="removeroleslabel"/>
                                                      </d:link>
                                 </security:protected-element>

                                                <c:if test="${actionBean.user.verified == 'NO'}">
                                                     <security:protected-element name="verify-users">
                                                           |
                                                           <d:link
                                                           href="/web/verify-user/${actionBean.user.id}/verify">
                                                           <fmt:message key="verifyuserlabel"/>
                                                           </d:link>
                                                     </security:protected-element>
                                                </c:if>
                                          </td>
                                      </tr>
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
