<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Security - Register">

    <s:layout-component name="head">
     </s:layout-component>

    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">

                                   <s:errors/>

                    <s:form action="/web/register1" name="createForm" id="createForm"
                            method="post">
                        <input type="hidden" name="_eventName" id="_eventName" value="save"/>
                        <table class="alternating">
                            <thead>
                            <tr>
                                <th colspan="2">
                                    <u><fmt:message key="registerasheaderlabel"/></u>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                                 <tr>
                                     <td>
                                         <fmt:message key="regtypelabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="regType" id="regType" style="background-color:#F0E68C">
                                                   <s:option value="">
                                                   <fmt:message key="selectregTypelabel"/></s:option>
                                                   <c:forEach items="${actionBean.regTypes}" var="regType"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.regType !=null &&
                                                             actionBean.regType == regType}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${regType}" ${selectedValue}/>
                                                      ${regType.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                     </td>
                                 </tr>
                    </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
