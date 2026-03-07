<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Job List">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper" style="background-color: #fdfdfd; border-radius: 10px 10px 10px 10px;overflow:hidden;">
            <div class="container">
                <div id="content">

                    <c:if test="${empty actionBean.jobs}">
                        <fmt:message key="nojobsfound"/>
                    </c:if>
                    <c:if test="${!empty actionBean.jobs}">
                        ${actionBean.jobsSize} <fmt:message key="jobsfound"/>
                    </c:if>
                    <table class="alternating">
                        <c:if test="${!empty actionBean.jobs}">
                            <thead>
                            <tr>
                                    <th align="left">
                                            <fmt:message key="datelabel"/>
                                    </th>
                                    <th align="left">
                                            <fmt:message key="categorylabel"/>
                                    </th>
                                    <th align="left">
                                            <fmt:message key="titlelabel"/>
                                    </th>
                                    <th align="left">
                                            <fmt:message key="namelabel"/>
                                    </th>
                                    <th align="left">
                                            <fmt:message key="emaillabel"/>
                                    </th>
                                    <th align="left">
                                           <fmt:message key="mobilelabel"/>
                                    </th>
                                    <th align="right">
                                        <fmt:message key="actionlabel"/>
                                    </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${actionBean.jobs}" var="job"
                                       varStatus="loopStatus">
                                <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                    <td align="left">
                                            ${job.formattedDateCreated}
                                    </td>
                                    <td align="left">
                                            ${job.category.name}
                                    </td>
                                    <td align="left">
                                            ${job.title}
                                    </td>
                                    <td align="left">
                                            ${job.name}
                                    </td>
                                    <td align="left">
                                            ${job.email}
                                    </td>
                                    <td align="left">
                                            ${job.mobile}
                                    </td>
                                    <td align="right">
                                                <d:link
                                                             href="/web/job-details?job=${job.id}">
                                                             <fmt:message key="detailslabel"/>
                                                 </d:link>
                                                 |
                                                 <d:link
                                                             href="/web/manage-job?job=${job.id}">
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

