<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Worker Details">

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
                                        <u><fmt:message key="workerdetailsheaderlabel"/></u>
                                    </th>
                                </tr>
                            </thead>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="namelabel"/>
                                </td>
                                <td>
                                    ${actionBean.worker.fullName}
                                </td>
                            </tr>
                              <tr>
                                  <td class="strong">
                                     <fmt:message key="emaillabel"/>
                                  </td>
                                  <td>
                                       <a href="mailto:${actionBean.worker.email}">${actionBean.worker.email}</a>
                                  </td>
                              </tr>
                              <tr>
                                    <td>
                                        <fmt:message key="profilelabel"/>
                                    </td>
                                        <td nowrap="nowrap">
                                                ${actionBean.worker.profile}
                                        </td>
                              </tr>
                            <tr>
                                    <td></td>
                                    <td>
                                                 <d:link
                                                             href="/web/worker-publications/${actionBean.worker.id}" target="_blank">
                                                             <fmt:message key="workerpublicationslabel"/>
                                                   </d:link>
                                         <security:protected-element name="manage-workers">
                                                  | <d:link
                                                             href="/web/edit-worker/${actionBean.worker.id}">
                                                             <fmt:message key="editlabel"/>
                                                   </d:link>
                                              <c:if test="${actionBean.worker.isShowcased == false}">
                                                 | <d:link
                                                             href="/web/innovators/${actionBean.worker.id}/add">
                                                             <fmt:message key="showcaselabel"/>
                                                   </d:link>
                                              </c:if>
                                               <c:if test="${actionBean.worker.isShowcased == true}">
                                                  | <d:link
                                                              href="/web/innovators/${actionBean.worker.id}/remove">
                                                              <fmt:message key="unshowcaselabel"/>
                                                    </d:link>
                                               </c:if>
                                         </security:protected-element>
                                    </td>
                                 </tr>
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
