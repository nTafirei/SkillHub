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
                                    <fmt:message key="namelabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.firstName}
                                    <security:protected-element name="view-deeper-user-details">
                                        ${actionBean.user.lastName}
                                    </security:protected-element>
                                </td>
                            </tr>

                            <tr>
                                <td class="strong">
                                    <fmt:message key="surbablabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.address.suburb.name}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="citylabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.address.suburb.city.name}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="countrylabel"/>
                                </td>
                                <td>
                                        ${actionBean.user.address.suburb.city.country}
                                </td>
                            </tr>

                            <security:protected-element name="view-deeper-user-details">
                                    <tr>
                                        <td class="strong">
                                           <fmt:message key="mobilephonelabel"/>
                                        </td>
                                        <td>
                                             <a href="tel:${actionBean.user.email}">${actionBean.user.mobilePhone}</a>
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
                            </security:protected-element>

                        <c:if test="${actionBean.user.isTalent}">
                              <tr>
                                  <td class="strong">
                                     <fmt:message key="skillslabel"/>
                                  </td>
                                  <td>
                                      ${actionBean.user.skillsAsString}
                                  </td>
                              </tr>

                              <tr>
                                  <td class="strong">
                                     <fmt:message key="desclabel"/>
                                  </td>
                                  <td>
                                      ${actionBean.user.description}
                                  </td>
                              </tr>
                        </c:if>

                        <security:protected-element name="view-deeper-user-details">
                            <tr>
                                <td class="strong">
                                    <fmt:message key="addresslabel"/>
                                </td>
                                <td>
                                    ${actionBean.user.address.address}
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
                                     <fmt:message key="verified"/>
                                  </td>
                                  <td>
                                      ${actionBean.user.verified}
                                  </td>
                            </tr>
                            </security:protected-element>
                            <tr>
                               <td><fmt:message key="actionlabel"/></td>
                               <td align="left">
                                     <security:protected-element name="view-deeper-user-details">
                                         <security:protected-element name="disable-users">
                                                              <d:link
                                                                  href="/web/disable/${actionBean.user.id}">
                                                                  <fmt:message key="disablelabel"/> |
                                                              </d:link>
                                         </security:protected-element>
                                         <security:protected-element name="manage-roles">
                                                              <d:link
                                                              href="/web/assign-role/${actionBean.user.id}">
                                                              <fmt:message key="assignroleslabel"/>
                                                          </d:link>
                                                         | <d:link
                                                              href="/web/remove-role/${actionBean.user.id}">
                                                              <fmt:message key="removeroleslabel"/> |
                                                          </d:link>
                                        </security:protected-element>

                                                <c:if test="${actionBean.user.verified == 'NO'}">
                                                     <security:protected-element name="verify-users">
                                                           <d:link
                                                           href="/web/verify-user/${actionBean.user.id}/verify">
                                                           <fmt:message key="verifyuserlabel"/>
                                                           </d:link> |
                                                     </security:protected-element>
                                                </c:if>
                                     </security:protected-element>
                                            <c:if test="${actionBean.user.isTalent}">
                                                    <d:link
                                                          href="/web/send-message?recipient=${actionBean.user.id}">
                                                          <fmt:message key="sendmessagelabel"/>
                                                      </d:link>
                                                      |
                                                      <d:link
                                                          href="/web/review/${actionBean.user.id}">
                                                          <fmt:message key="reviewlabel"/>
                                                      </d:link>
                                                      |
                                                      <d:link
                                                          href="/web/reviews/${actionBean.user.id}">
                                                          <fmt:message key="reviewslabel"/>
                                                      </d:link>
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
