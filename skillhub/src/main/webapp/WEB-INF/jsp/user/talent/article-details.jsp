<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Article Details">

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
                                        <u><fmt:message key="pubdetailsheaderlabel"/></u>
                                    </th>
                                </tr>
                            </thead>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="titlelabel"/>
                                </td>
                                <td>
                                    ${actionBean.article.title}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="categorylabel"/>
                                </td>
                                <td>
                                    ${actionBean.article.category.name}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="pubdatelabel"/>
                                </td>
                                <td>
                                    ${actionBean.article.formattedPubDate}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="sourcelabel"/>
                                </td>
                                <td>
                                    ${actionBean.article.source}
                                </td>
                            </tr>
                            <tr>
                                 <td class="strong">
                                    <fmt:message key="workerslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.article.workers}" var="worker"
                                                                                varStatus="loopStatus">
                                     ${worker.fullName}
                                                <c:if test="${funcs:hasMore(loopStatus.index,actionBean.article.workers) == true}">
                                                 ,&nbsp;&nbsp;
                                                </c:if>
                                     </c:forEach>
                                 </td>
                             </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="summarylabel"/>
                                </td>
                                <td>
                                    ${actionBean.article.summary}
                                </td>
                            </tr>

                            <tr>
                                 <td class="strong">
                                    <fmt:message key="citationslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.article.citations}" var="citation"
                                                                                varStatus="loopStatus">
                                        ${citation}<br/>
                                     </c:forEach>
                                 </td>
                             </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="actionlabel"/>
                                </td>
                                <td>
                                            <d:link href="/web/comments/${actionBean.article.id}">
                                                <fmt:message key="commentslabel"/></d:link>
                                            |
                                            <d:link href="/web/comment/${actionBean.article.id}">
                                                <fmt:message key="addcommentlabel"/></d:link>
                                            | <d:link href="/web/download-attachment?attachment=${actionBean.article.attachment.id}" target="_blank">
                                                <fmt:message key="downloadlabel"/></d:link>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
