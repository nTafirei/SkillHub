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

                    <s:form action="/web/register2" name="createForm" id="createForm"
                            method="post">
                        <input type="hidden" name="_eventName" id="_eventName" value="save"/>
                        <table class="alternating">
                            <thead>
                            <tr>
                                <th colspan="2">
                                    <u><fmt:message key="registerheaderlabel"/></u>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                                 <tr>
                                     <td>
                                         <fmt:message key="provincelabel"/>
                                     </td>
                                     <td>

                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="province" id="province" style="background-color:#F0E68C"
                                             onchange="resetDistrict()">
                                                   <s:option value="">
                                                   <fmt:message key="selectprovincelabel"/></s:option>
                                                   <c:forEach items="${actionBean.provinces}" var="province"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.province !=null &&
                                                             actionBean.province.id == province.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${province.id}" ${selectedValue}/>
                                                      ${province.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td>
                                         <fmt:message key="districtlabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="district" id="district" style="background-color:#F0E68C"
                                              onchange="resetWard()">
                                                   <s:option value="">
                                                   <fmt:message key="selectdistrictlabel"/></s:option>
                                                   <c:forEach items="${actionBean.districts}" var="district"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.district !=null &&
                                                             actionBean.district.id == district.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${district.id}" ${selectedValue}/>
                                                      ${district.name}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                     </td>
                                 </tr>
                                 <tr>
                                      <td>
                                          <fmt:message key="wardlabel"/>
                                      </td>
                                      <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="ward" id="ward" style="background-color:#F0E68C"
                                              onchange="createForm.submit()">
                                                   <s:option value="">
                                                   <fmt:message key="selectwardlabel"/></s:option>
                                                   <c:forEach items="${actionBean.wards}" var="ward"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.ward !=null &&
                                                             actionBean.ward.id == ward.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${ward.id}" ${selectedValue}/>
                                                      ${ward.wardNumber}
                                                      <c:set var="selectedValue" value=""/>
                                                   </c:forEach>
                                             </s:select>
                                      </td>
                                 </tr>
                            </tbody>
                        </table>
                        <script language="javascript">
                            function resetDistrict(){
                                document.getElementById('district').selectedIndex = 0;
                                document.getElementById('ward').selectedIndex = 0;
                                document.createForm.submit();
                            }
                            function resetWard(){
                                document.getElementById('ward').selectedIndex = 0;
                                document.createForm.submit();
                            }
                        </script>
                    </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
