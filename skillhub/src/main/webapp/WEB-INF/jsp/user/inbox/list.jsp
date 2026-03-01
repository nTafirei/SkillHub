<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Inbox">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper" style="background-color: #fdfdfd; border-radius: 10px 10px 10px 10px;overflow:hidden;">
            <div class="container">
                <div id="content">

                    <c:if test="${empty actionBean.notifications}">
                        <fmt:message key="noinboxmessagesfound"/>
                    </c:if>
                    <table class="alternating">
                        <c:if test="${!empty actionBean.notifications}">
                            <thead>
                            <tr>
                                <th align="left">
                                    <fmt:message key="subjectlabel"/>
                                </th>
                                <th align="left">
                                    <fmt:message key="datecreatedlabel"/>
                                </th>
                                <th align="left">
                                    <fmt:message key="bodylabel"/>
                                </th>
                                <th align="left">
                                    <fmt:message key="actionlabel"/>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${actionBean.notifications}" var="message"
                                       varStatus="loopStatus">
                                <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                    <td>
                                            ${message.subject}
                                    </td>
                                    <td>
                                            ${message.dateCreated}
                                    </td>
                                    <td>
                                            ${message.body}
                                    </td>
                                    <td>
                                                 <d:link
                                                             href="/web/inbox/{message.id}?_eventName=delete">
                                                             <fmt:message key="deletelabel"/>
                                                 </d:link>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>

