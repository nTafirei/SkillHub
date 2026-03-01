<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Publications">

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
                            <fmt:message key="nopublicationsfound"/>
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.publications}">
                                        <strong>${actionBean.publicationsSize}
                                        <fmt:message key="publicationsfound"/></strong>
                                    </c:if>
                                </td>
                                <td align="center" colspan="3">

                                    <d:link href="/web/submit-publication">
                                    <fmt:message key="uploadlabel"/></d:link>
                                    |
                                    <d:link href="/web/search-by-title">
                                    <fmt:message key="searchbytitlelabel"/></d:link>
                                    |
                                    <d:link href="/web/search-by-worker">
                                    <fmt:message key="searchbyworkerlabel"/></d:link>
                                    |
                                    <d:link href="/web/search-by-summary">
                                    <fmt:message key="searchbysummarylabel"/></d:link>
                                </td>
                                        <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="category" id="category" style="background-color:#F0E68C" onchange="document.searchForm.submit();">
                                                   <s:option value="">
                                                   <fmt:message key="selectcategorylabel"/></s:option>
                                                    <c:forEach items="${actionBean.categories}" var="category"
                                                                     varStatus="loopStatus">
                                                       <c:if test="${actionBean.category !=null &&
                                                             actionBean.category == category}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${category}" ${selectedValue}/>
                                                      ${category.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
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
                                        <fmt:message key="sourcelabel"/>
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
                                            <d:link href="/web/publications?category=${publication.category}&_eventName=view" target="_blank">
                                                ${publication.category.name}</d:link>
                                        </td>

                                        <td>
                                            <d:link href="/web/search-by-source?source=${publication.source}&_eventName=search" target="_blank">
                                                ${publication.source}</d:link>
                                        </td>

                                        <td>
                                                ${publication.formattedPubDate}
                                        </td>
                                        <td nowrap="nowrap">
                                             <c:forEach items="${publication.workers}" var="worker"
                                                                                        varStatus="loopStatus">

                                                 <d:link
                                                             href="/web/worker-publications/${worker.id}" target="_blank">
                                                             ${worker.fullName}
                                                   </d:link>
                                                <c:if test="${funcs:hasMore(loopStatus.index,publication.workers) == true}">
                                                 , &nbsp;&nbsp;
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
                                                    <d:link href="/web/manage-showcase?publication=${publication.id}&_eventName=add&category=${actionBean.category}">
                                                        <fmt:message key="showcaselabel"/></d:link>
                                                </security:protected-element>
                                            </c:if>
                                            <c:if test="${publication.isShowcased == true}">
                                                <security:protected-element name="manage-showcases">
                                                     |
                                                    <d:link href="/web/manage-showcase?publication=${publication.id}&_eventName=remove&category=${actionBean.category}">
                                                        <fmt:message key="unshowcaselabel"/></d:link>
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
