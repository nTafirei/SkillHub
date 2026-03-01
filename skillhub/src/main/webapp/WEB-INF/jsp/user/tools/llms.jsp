<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Languange Models">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                    <security:protected-element name="show-language-models">
                        <c:if test="${empty actionBean.models}">
                           <strong> <fmt:message key="nomodelsfound"/></strong>
                        </c:if>
                        <table width="100%">
                            <tr>
                                <td colspan="4" align="center">
                                     <strong>${actionBean.modelsSize} <fmt:message key="modelsfound"/></strong>
                                </td>
                            </tr>
                        </table>
                        <table class="alternating">
                            <c:if test="${!empty actionBean.models}">
                                <thead>
                                <tr>
                                    <td>
                                        <fmt:message key="namelabel"/>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actionBean.models}" var="languageModel"
                                           varStatus="loopStatus">
                                    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                        <td>
                                                ${languageModel.name}
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:if>
                        </table>
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
