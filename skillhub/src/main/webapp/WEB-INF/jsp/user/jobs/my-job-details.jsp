<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="My Job Details">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>

                        <table class="alternating">
                                <tr>
                                    <td colspan="2" align="center">
                                        <fmt:message key="myjoblabel"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="titlelabel"/>
                                    </td>
                                    <td>
                                        ${actionBean.job.title}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="namelabel"/>(will be hidden to the public)
                                    </td>
                                    <td>
                                        ${actionBean.job.name}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="mobilelabel"/><br/> (will be hidden to the public)
                                    </td>
                                    <td>
                                        ${actionBean.job.mobile}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="emaillabel"/> <br/>(will be hidden to the public)
                                    </td>
                                    <td>
                                        ${actionBean.job.email}
                                    </td>
                                </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="categorylabel"/>
                                     </td>
                                     <td>
                                        ${actionBean.job.category.name}
                                     </td>
                                 </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="citylabel"/>
                                     </td>
                                     <td>
                                        ${actionBean.job.suburb.city.name}
                                     </td>
                                 </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="surbablabel"/>
                                     </td>
                                     <td>
                                        ${actionBean.job.suburb.name}
                                     </td>
                                 </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="jobdesclabel"/>
                                    </td>
                                    <td>
                                        ${actionBean.job.desc}
                                    </td>
                                </tr>
                        </table>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
