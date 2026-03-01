<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Search Workers">

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
                                <s:form action="/web/search-workers" method="post" name="searchForm" id="searchForm">
                                <input type="hidden" name="target" value="${actionBean.target}"/>
                                <input type="hidden" name="_eventName" value="search"/>

                                <table>
                                    <thead>
                                    <tr>
                                        <th colspan="2">
                                            <h4><fmt:message key="searchworkersmenulabel"/></h4>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr valign="top">
                                        <td nowrap="nowrap"><fmt:message key="firstnamelabel"/></td>
                                        <td nowrap="nowrap">
                                            <d:text style="background-color:#F0E68C" name="firstName"
                                            id="firstName"/>
                                        </td>
                                    </tr>
                                     <tr valign="top">
                                         <td nowrap="nowrap"><fmt:message key="lastnamelabel"/>
                                          </td>
                                         <td nowrap="nowrap">
                                             <d:text style="background-color:#F0E68C" name="lastName"
                                             id="lastName"/>
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
