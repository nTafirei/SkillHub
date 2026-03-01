<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Publication Comments">

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
                                    ${actionBean.publication.title}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="categorylabel"/>
                                </td>
                                <td>
                                    ${actionBean.publication.category}
                                </td>
                            </tr>
                            <tr>
                                <td class="strong">
                                    <fmt:message key="pubdatelabel"/>
                                </td>
                                <td>
                                    ${actionBean.publication.formattedPubDate}
                                </td>
                            </tr>
                            <tr>
                                 <td class="strong">
                                    <fmt:message key="workerslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.publication.workers}" var="worker"
                                                                                varStatus="loopStatus">
                                     ${worker.fullName}
                                                <c:if test="${funcs:hasMore(loopStatus.index,publication.workers) == true}">
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
                                    ${actionBean.publication.summary}
                                </td>
                            </tr>
                              <c:if test="${!empty actionBean.publication.sortedComments}">
                                <tr>
                                    <td colspan="3">
                                        <table>
                                            <c:forEach items="${actionBean.publication.sortedComments}" var="comment"
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
                                                                <d:link href="/web/comment/${actionBean.publication.id}/${comment.id}">                                                                                                                                <fmt:message key="replylabel"/></d:link>
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
                                            <d:link href="/web/comment/${actionBean.publication.id}/">
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
