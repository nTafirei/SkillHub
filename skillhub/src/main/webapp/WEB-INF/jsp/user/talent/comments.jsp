<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Article Comments">

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
                                        <u><fmt:message key="pubcommentslabel"/></u>
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
                                    ${actionBean.article.category}
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
                                    <fmt:message key="workerslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.article.workers}" var="worker"
                                                                                varStatus="loopStatus">
                                     ${worker.fullName}
                                                <c:if test="${funcs:hasMore(loopStatus.index,article.workers) == true}">
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
                              <c:if test="${!empty actionBean.article.sortedComments}">
                                <tr>
                                    <td colspan="3">
                                        <table>
                                            <c:forEach items="${actionBean.article.sortedComments}" var="comment"
                                                       varStatus="loopStatus">
                                                <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                                            <td>
                                                                <fmt:message key="commentbylabel"/> ${comment.createdBy.initials}
                                                            </td>
                                                            <td>
                                                                ${comment.title}
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <fmt:message key="remarkslabel"/>
                                                            </td>
                                                            <td>
                                                                ${comment.body}
                                                                <br/>
                                                                <d:link href="/web/comment/${actionBean.article.id}/${comment.id}">                                                                                                                                <fmt:message key="replylabel"/></d:link>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                <table>
                                            </td>
                                        </tr>
                                  </c:if>
                            <tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="actionlabel"/>
                                </td>
                                <td>
                                            <d:link href="/web/comment/${actionBean.article.id}/">
                                                <fmt:message key="addcommentlabel"/></d:link>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
