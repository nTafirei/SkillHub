<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Publication Details">

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
                                    ${actionBean.publication.pubDate}
                                </td>
                            </tr>
                            <tr>
                                 <td class="strong">
                                    <fmt:message key="workerslabel"/>
                                 </td>
                                 <td>
                                     <c:forEach items="${actionBean.publication.workers}" var="worker"
                                                                                varStatus="loopStatus">
                                     ${worker.fullName} ,&nbsp;&nbsp;
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
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
