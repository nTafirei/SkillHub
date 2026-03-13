<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Search For Talent">

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
                                <s:form action="/web/search-for-talent" method="post" name="searchForm" id="searchForm">
                                <input type="hidden" name="target" value="${actionBean.target}"/>
                                <input type="hidden" name="_eventName" id="_eventName" value="search"/>

                                <table>
                                    <thead>
                                    <tr>
                                        <th colspan="2">
                                            <h4><fmt:message key="searchtalentmenulabel"/></h4>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                 <tr>
                                     <td>
                                         <fmt:message key="categorylabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="category" id="category" style="background-color:#F0E68C"
                                             onchange="submitForm()">
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
                                         <fmt:message key="skilllabel"/>
                                     </td>
                                     <td>
                                             <c:set var="selectedValue" value=""/>
                                             <s:select name="skill" id="skill" style="background-color:#F0E68C">
                                                   <s:option value="">
                                                   <fmt:message key="selectskilllabel"/></s:option>
                                                   <c:forEach items="${actionBean.skills}" var="skill"
                                                              varStatus="loopStatus">

                                                       <c:if test="${actionBean.skill !=null &&
                                                             actionBean.skill.id == skill.id}">
                                                         <c:set var="selectedValue" value="selected"/>
                                                      </c:if>
                                                       <option value="${skill.id}" ${selectedValue}/>
                                                      ${skill.name}
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
                                        <td>&nbsp;</td>
                                        <td align="right">
                                            <fmt:message key="submitlabel" var="submitlabel"/>
                                            <d:submit name="submitbutton" class="small" value="${submitlabel}" id="login"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                </s:form>
                        </div>
                    </div>
                </div>
                        <script language="javascript">
                            function submitForm(){
                                document.getElementById('_eventName').value = 'populate';
                                document.getElementById('searchForm').submit();
                            }
                        </script>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
