<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Workers">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                    <security:protected-element name="view-workers">
                        <c:if test="${empty actionBean.workers}">
                            <fmt:message key="noworkersfound"/>
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.workers}">
                                        <strong>${actionBean.workersSize}
                                        <fmt:message key="workersfound"/></strong>
                                    </c:if>
                                </td>
                                <td align="center" colspan="3"><d:link href="/web/search-workers"><fmt:message key="searchworkersmenulabel"/></d:link></td>
                            </tr>
                        </table>
                        <table class="alternating">
                            <c:if test="${!empty actionBean.workers}">
                                <thead>
                                <tr>
                                    <td>
                                        <fmt:message key="namelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="imagelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="emaillabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="profilelabel"/>
                                    </td>
                                    <td align="right">
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actionBean.workers}" var="worker"
                                           varStatus="loopStatus">
                                    <tr valign="top" class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td nowrap="nowrap">
                                                ${worker.fullName}
                                        </td>
                                        <td style="vertical-align: top; important!">
                                            <c:if test="${worker.hasPicture}">
                                                <img src="/web/download-attachment?attachment=${worker.picture.id}" height="50" width="50"/>
                                            </c:if>
                                            <c:if test="${!worker.hasPicture}">
                                                <img src="${actionBean.imagePath}/not-available.png" height="80" width="80"/>
                                            </c:if>
                                        </td>
                                        <td>
                                               <a href="mailto:${worker.email}">${worker.email}</a>
                                        </td>
                                        <td nowrap="nowrap">
                                                ${worker.profile}
                                        </td>
                                         <td align="right" nowrap="nowrap">

                                                 <d:link
                                                             href="/web/worker-publications/${worker.id}" target="_blank">
                                                             <fmt:message key="workerpublicationslabel"/>
                                                   </d:link>

                                                 | <d:link
                                                             href="/web/worker-details/${worker.id}">
                                                             <fmt:message key="detaillabel"/>
                                                   </d:link>
                                         <security:protected-element name="manage-workers">
                                                 | <d:link
                                                             href="/web/edit-worker/${worker.id}">
                                                             <fmt:message key="editlabel"/>
                                                   </d:link>
                                              <br/>
                                              <c:if test="${worker.isShowcased == false}">
                                                 | <d:link
                                                             href="/web/innovators/${worker.id}/add">
                                                             <fmt:message key="showcaselabel"/>
                                                   </d:link>
                                              </c:if>
                                               <c:if test="${worker.isShowcased == true}">
                                                  | <d:link
                                                              href="/web/innovators/${worker.id}/remove">
                                                              <fmt:message key="unshowcaselabel"/>
                                                    </d:link>
                                               </c:if>
                                         </security:protected-element>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:if>
                        </table>
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
