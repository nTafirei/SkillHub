<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Post a Job">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <div id="main-wrapper main-contents">
            <div class="container">
                <div id="content">
                    <s:errors/>

                        <s:form action="/web/post" method="post" name="searchForm" id="searchForm">
                        <input type="hidden" name="target" value="${actionBean.target}"/>
                        <input type="hidden" id="_eventName" name="_eventName" value="save"/>

                        <table class="alternating">
                                <tr>
                                    <td colspan="2" align="center">
                                        <fmt:message key="postlabel"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="titlelabel"/>
                                    </td>
                                    <td>
                                        <d:text name="title" id="title" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="namelabel"/><br/>(will be hidden to the public)
                                    </td>
                                    <td>
                                        <d:text name="name" id="name" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="mobilelabel"/><br/> (will be hidden to the public)
                                    </td>
                                    <td>
                                        <d:text name="mobile" id="mobile" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="emaillabel"/> <br/>(will be hidden to the public)
                                    </td>
                                    <td>
                                        <d:text name="email" id="email" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="categorylabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="category" id="category" style="background-color:#F0E68C">
                                                   <s:option value="">
                                                   <fmt:message key="selectcategorylabel"/></s:option>
                                                   <c:forEach items="${actionBean.categories}" var="category"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.category !=null &&
                                                             actionBean.category.id == category.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${category.id}" ${selectedValue}/>
                                                      ${category.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="citylabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="city" id="city" style="background-color:#F0E68C"
                                             onchange="submitForm()">
                                                   <s:option value="">
                                                   <fmt:message key="selectcitylabel"/></s:option>
                                                   <c:forEach items="${actionBean.cities}" var="city"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.city !=null &&
                                                             actionBean.city.id == city.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${city.id}" ${selectedValue}/>
                                                      ${city.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="surbablabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="suburb" id="suburb" style="background-color:#F0E68C">
                                                   <s:option value="">
                                                   <fmt:message key="selectsurbablabel"/></s:option>
                                                   <c:forEach items="${actionBean.suburbs}" var="suburb"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.suburb !=null &&
                                                             actionBean.suburb.id == suburb.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${suburb.id}" ${selectedValue}/>
                                                      ${suburb.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                     </td>
                                 </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="jobdesclabel"/>
                                    </td>
                                    <td>
                                        <d:textarea name="desc" id="desc" style="background-color:#F0E68C"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="actionlabel"/>
                                    </td>
                                        <td align="right">
                                            <fmt:message key="submitlabel" var="submitlabel"/>
                                            <d:submit name="submitbtn" class="small" value="${submitlabel}" id="login"/>
                                        </td>
                                    </tr>
                        </table>
                      </s:form>
            </div>
                                <script language="javascript">
                                    function submitForm(){
                                        document.getElementById('_eventName').value = 'populate';
                                        document.getElementById('searchForm').submit();
                                    }
                                </script>
        </div>
    </s:layout-component>
</s:layout-render>
