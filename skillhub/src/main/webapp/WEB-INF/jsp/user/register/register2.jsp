<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Security - Register">

    <s:layout-component name="head">
     </s:layout-component>

    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                            <c:if test="${!empty actionBean.context.validationErrors}">
                                   <s:errors/>
                            </c:if>
                    <s:form action="/web/register2" name="createForm" id="createForm"
                            method="post">
                         <input type="hidden" name="_eventName" id="_eventName" value="save"/>
                         <input type="hidden" name="regType" id="regType" value="${actionBean.regType}"/>

                        <table class="alternating">
                            <thead>
                            <tr>
                                <th colspan="2">
                                    <u><fmt:message key="regtypelabel"/> ${actionBean.regType.name}</u>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <td>
                                    <fmt:message key="firstnamelabel"/>
                                    <input type = "text"  style="background-color:#F0E68C"
                                    name="firstName" value="${actionBean.firstName}"/>

                                </td>
                                <td>
                                    <fmt:message key="middlenamelabel"/>
                                    <input type = "text"  style="background-color:#F0E68C"
                                    name="middleName" value="${actionBean.middleName}"/>

                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="lastnamelabel"/> <br/> <i>(Will be hidden from the public)</i>
                                    <input type = "text" style="background-color:#F0E68C"
                                    name="lastName" value="${actionBean.lastName}"/>

                                </td>
                                <td>
                                    <fmt:message key="passwordlabel"/>
                                    <input type = "password"  style="background-color:#F0E68C"
                                    name="password"/>

                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="addresslabel"/>
                                    <input type = "text"  style="background-color:#F0E68C"
                                    name="address" value="${actionBean.address}"/>
                                </td>
                                <td>
                                    <fmt:message key="townlabel"/>
                                    <input type = "text" style="background-color:#F0E68C"
                                    name="town" value="${actionBean.town}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="nationalidlabel"/>
                                    <input type = "text" style="background-color:#F0E68C"
                                    name="nationalId" value="${actionBean.nationalId}"/>
                                </td>
                               <td>
                                    <fmt:message key="mobilephonelabel"/>
                                    <input type = "text" style="background-color:#F0E68C"
                                    name="mobilephone" value="${actionBean.mobilephone}"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" colspan="4">
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
