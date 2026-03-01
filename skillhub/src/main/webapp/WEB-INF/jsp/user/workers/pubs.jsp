<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Worker Publications">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>
                    <security:protected-element name="view-publications">
                      <s:form action="/web/publications" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" name="_eventName" value="view"/>
                        <c:if test="${empty actionBean.publications}">
                            <fmt:message key="nopublicationsfound"/> for ${actionBean.worker.fullName}
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.publications}">
                                        <strong>${actionBean.publicationsSize}
                                        <fmt:message key="publicationsfound"/> for ${actionBean.worker.fullName}</strong>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                        <table class="alternating">
                            <c:if test="${!empty actionBean.publications}">
                                <thead>
                                <tr>
                                    <td>
                                        <fmt:message key="titlelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="categorylabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="pubdatelabel"/>
                                    </td>
                                    <td>
                                        <fmt:message key="workerslabel"/>
                                    </td>
                                    <td align="right">
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actionBean.publications}" var="publication"
                                           varStatus="loopStatus">
                                    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td>
                                                ${publication.title}
                                        </td>
                                        <td>
                                                ${publication.category.name}
                                        </td>
                                        <td>
                                                ${publication.pubDate}
                                        </td>
                                        <td nowrap="nowrap">
                                             <c:forEach items="${publication.workers}" var="worker"
                                                                                        varStatus="loopStatus">
                                                ${worker.fullName}
                                                <c:if test="${funcs:hasMore(loopStatus.index,publication.workers) == true}">
                                                 ,&nbsp;&nbsp;
                                                </c:if>

                                             </c:forEach>
                                        </td>
                                         <td align="right">
                                            <d:link href="/web/download-attachment?attachment=${publication.attachment.id}" target="_blank">
                                                <fmt:message key="downloadlabel"/></d:link>
                                             |
                                            <d:link href="/web/pub-details/${publication.id}" target="_blank">
                                                <fmt:message key="detailslabel"/></d:link>

                                            <c:if test="${publication.isShowcased == false}">
                                                <security:protected-element name="manage-showcases">
                                                     |
                                                    <d:link href="/web/showcases?publication=${publication.id}&_eventName=add">
                                                        <fmt:message key="showcaselabel"/></d:link>
                                                </security:protected-element>
                                            </c:if>
                                         </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:if>
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
