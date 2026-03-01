<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Chat Tool">

    <s:layout-component name="head">
     </s:layout-component>

    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                                   <s:errors/>

                    <c:if test="${!empty actionBean.answer}">
                      <strong>
                      <fmt:message key="requestlabel"/> ${actionBean.request}<br/>
                      <fmt:message key="answerlabel"/> ${actionBean.answer}</strong>
                    </c:if>

                    <s:form action="/web/chat" name="createForm" id="createForm"
                            method="post">
                        <input type="hidden" name="_eventName" id="_eventName" value="search"/>
                        <table class="alternating">
                            <thead>
                            <tr>
                                <th colspan="2">
                                    <fmt:message key="enterchatprompt"/>
                               </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                     <d:textarea style="background-color:#F0E68C" name="request"/>
                                </td>
                                <td align="right">
                                    <fmt:message key="submitlabel" var="submitlabel"/>
                                    <d:submit class="small" name="save" value="${submitlabel}"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
