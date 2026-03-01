<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Search Publications By Title">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">


        <!-- Main -->
        <div id="main-wrapper">
            <div class="container">
                <div class="row 200%">
                    <div class="8u important(collapse)">
                        <div id="content">
                            <div class="row">
                                <section class="6u 12u(narrower)">
                                    <div class="box post">
                                        <div class="inner">
                                            <s:errors/>
                                        </div>
                                    </div>
                                </section>
                            </div>
                            <!-- Content -->
                            <article>
                                <table>
                                    <tr>
                                        <td align="center" colspan="3">
                                            <d:link href="/web/search-by-worker">
                                            <fmt:message key="searchbyworkerlabel"/></d:link>
                                            |
                                            <d:link href="/web/search-by-summary">
                                            <fmt:message key="searchbysummarylabel"/></d:link>
                                        </td>
                                    </tr>
                                </table>
                                <s:form action="/web/search-by-title" method="post" name="searchForm" id="searchForm">
                                <input type="hidden" name="target" value="${actionBean.target}"/>
                                <input type="hidden" name="_eventName" value="search"/>

                                <table>
                                    <thead>
                                    <tr>
                                        <th colspan="2">
                                            <h4><fmt:message key="searchpubsbytitlelabel"/></h4>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr valign="top">
                                        <td nowrap="nowrap"><fmt:message key="titlelabel"/></td>
                                        <td nowrap="nowrap">
                                            <d:text style="background-color:#F0E68C" name="title"
                                            id="title"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                        <td align="right">
                                            <fmt:message key="submitlabel" var="submitlabel"/>
                                            <d:submit name="submit" class="small" value="${submitlabel}" id="login"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
