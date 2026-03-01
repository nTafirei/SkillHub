<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Showcased Publications">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                    <security:protected-element name="view-publications">
                        <c:if test="${empty actionBean.publications}">
                            <fmt:message key="noshowcasepublicationsfound"/>
                        </c:if>
                        <s:form action="/web/showcases" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" name="_eventName" value="search"/>

                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.publications}">
                                        <strong>${actionBean.publicationsSize}
                                        <fmt:message key="showcasepublicationsfound"/></strong>
                                    </c:if>
                                </td>
                            </tr>
                                <tr>
                                        <td align="center" colspan = "3">
                                            <fmt:message key="categorylabel"/>
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
                                <tbody>
                                <c:forEach items="${actionBean.publications}" var="publication"
                                           varStatus="loopStatus">
                                    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td>
                                                <u>${publication.title}</u>
                                                <br/>
                                                <fmt:message key="datelabel"/>: ${publication.pubDate}
                                                <br/>
                                                <fmt:message key="categorylabel"/>: ${publication.category.name}
                                                <br/>
                                                <fmt:message key="sourcelabel"/>: ${publication.source}
                                                <br/>
                                                By:
                                              <c:forEach items="${publication.workers}" var="worker"
                                                                                         varStatus="loopStatus">
                                              ${worker.fullName}
                                                 <c:if test="${funcs:hasMore(loopStatus.index,publication.workers) == true}">
                                                  ,&nbsp;&nbsp;
                                                 </c:if>
                                              </c:forEach>
                                              <br/>
                                        </td>
                                        <td>
                                                ${publication.summary}
                                        </td>
                                         <td align="right" nowrap="nowrap">
                                            <d:link href="/web/download-attachment?attachment=${publication.attachment.id}" target="_blank">
                                                <fmt:message key="downloadlabel"/></d:link>
                                             <br/>
                                            <d:link href="/web/pub-details/${publication.id}" target="_blank">
                                                <fmt:message key="detailslabel"/></d:link>
                                             <br/>
                                            <d:link href="/web/comments/${publication.id}">
                                                <fmt:message key="commentslabel"/></d:link>
                                        <security:protected-element name="manage-showcases">
                                             <br/>
       <d:link href="/web/manage-showcase?publication=${publication.id}&_eventName=remove&category=${actionBean.category}">
                                                <fmt:message key="unshowcaselabel"/></d:link>
                                        </security:protected-element>
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
