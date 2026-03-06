<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Articles">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>
                    <security:protected-element name="view-articles">
                      <s:form action="/web/articles" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" name="_eventName" value="view"/>
                        <c:if test="${empty actionBean.articles}">
                            <fmt:message key="noarticlesfound"/>
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td align="center" colspan = "3">
                                    <c:if test="${!empty actionBean.articles}">
                                        <strong>${actionBean.articlesSize}
                                        <fmt:message key="articlesfound"/></strong>
                                    </c:if>
                                </td>
                                <td align="center" colspan="3">

                                    <d:link href="/web/submit-article">
                                    <fmt:message key="uploadlabel"/></d:link>
                                    |
                                    <d:link href="/web/search-by-title">
                                    <fmt:message key="searchbytitlelabel"/></d:link>
                                    |
                                    <d:link href="/web/search-by-author">
                                    <fmt:message key="searchbyauthorlabel"/></d:link>
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
                            <c:if test="${!empty actionBean.articles}">
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
                                        <fmt:message key="authorslabel"/>
                                    </td>
                                    <td align="right">
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actionBean.articles}" var="article"
                                           varStatus="loopStatus">
                                    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td>
                                                ${article.title}
                                        </td>
                                        <td>
                                            <d:link href="/web/articles?category=${article.category}&_eventName=view" target="_blank">
                                                ${article.category.name}</d:link>
                                        </td>

                                        <td>
                                            <d:link href="/web/search-by-source?source=${article.source}&_eventName=search" target="_blank">
                                                ${article.source}</d:link>
                                        </td>

                                        <td>
                                                ${article.formattedPubDate}
                                        </td>
                                        <td nowrap="nowrap">
                                             <c:forEach items="${article.authors}" var="author"
                                                                                        varStatus="loopStatus">

                                                 <d:link
                                                             href="/web/author-articles/${author.id}" target="_blank">
                                                             ${author.fullName}
                                                   </d:link>
                                                <c:if test="${funcs:hasMore(loopStatus.index,article.authors) == true}">
                                                 , &nbsp;&nbsp;
                                                </c:if>
                                             </c:forEach>
                                        </td>
                                         <td align="right">
                                            <d:link href="/web/download-attachment?attachment=${article.attachment.id}" target="_blank">
                                                <fmt:message key="downloadlabel"/></d:link>
                                             |
                                            <d:link href="/web/pub-details/${article.id}" target="_blank">
                                                <fmt:message key="detailslabel"/></d:link>

                                            <c:if test="${article.isShowcased == false}">
                                                <security:protected-element name="manage-showcases">
                                                     |
                                                    <d:link href="/web/manage-showcase?article=${article.id}&_eventName=add&category=${actionBean.category}">
                                                        <fmt:message key="showcaselabel"/></d:link>
                                                </security:protected-element>
                                            </c:if>
                                            <c:if test="${article.isShowcased == true}">
                                                <security:protected-element name="manage-showcases">
                                                     |
                                                    <d:link href="/web/manage-showcase?article=${article.id}&_eventName=remove&category=${actionBean.category}">
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
