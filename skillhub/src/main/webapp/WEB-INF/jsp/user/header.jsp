<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<!-- Header -->
<div id="header-wrapper">
    <header id="header" class="container">

        <!-- Logo -->

        <br/><br/>
        <div id="logo">
            <h1>
                <d:link href="/web/inbox"><fmt:message key="appnamekey"/></d:link>
            </h1>
        </div>

        <!-- Nav -->
        <nav id="nav">
            <ul>
                 <c:if test="${actionBean.isLoggedIn == false}">
                    <c:if test="${actionBean.navSection == 'register'}">
                        <li class="current"><d:link href="/web/register2"><fmt:message key="registermenu"/></d:link></li>
                    </c:if>
                    <c:if test="${actionBean.navSection != 'register'}">
                            <li ><d:link href="/web/register2"><fmt:message key="registermenu"/></d:link></li>
                    </c:if>
                </c:if>
                    <security:protected-element name="show-tools">
                         <c:if test="${actionBean.navSection == 'tools'}">
                             <li class="current"><d:link href="/web/tools"><fmt:message key="toolslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'tools'}">
                             <li><d:link href="/web/tools"><fmt:message key="toolslabel"/></d:link></li>
                         </c:if>
                    </security:protected-element>
                <c:if test="${actionBean.isLoggedIn == true}">
                         <c:if test="${actionBean.navSection == 'innovators'}">
                             <li class="current"><d:link href="/web/innovators"><fmt:message key="innovatorslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'innovators'}">
                             <li><d:link href="/web/innovators"><fmt:message key="innovatorslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection == 'showcases'}">
                             <li class="current"><d:link href="/web/showcases"><fmt:message key="showcaseslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'showcases'}">
                             <li><d:link href="/web/showcases"><fmt:message key="showcaseslabel"/></d:link></li>
                         </c:if>
                    <security:protected-element name="manage-publications">
                         <c:if test="${actionBean.navSection == 'inactive-publications'}">
                             <li class="current"><d:link href="/web/inactive-publications"><fmt:message key="inactivepublicationslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'inactive-publications'}">
                             <li><d:link href="/web/inactive-publications"><fmt:message key="inactivepublicationslabel"/></d:link></li>
                         </c:if>
                    </security:protected-element>
                    <security:protected-element name="view-publications">
                         <c:if test="${actionBean.navSection == 'publications'}">
                             <li class="current"><d:link href="/web/publications"><fmt:message key="publicationslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'publications'}">
                             <li><d:link href="/web/publications"><fmt:message key="publicationslabel"/></d:link></li>
                         </c:if>
                    </security:protected-element>
                    <security:protected-element name="view-workers">
                         <c:if test="${actionBean.navSection == 'workers'}">
                             <li class="current"><d:link href="/web/workers"><fmt:message key="workerslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'workers'}">
                             <li><d:link href="/web/workers"><fmt:message key="workerslabel"/></d:link></li>
                         </c:if>
                    </security:protected-element>
                    <security:protected-element name="generate-reports">
                         <c:if test="${actionBean.navSection == 'reports'}">
                             <li class="current"><d:link href="/web/reports"><fmt:message key="reportslabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'reports'}">
                             <li><d:link href="/web/reports"><fmt:message key="reportslabel"/></d:link></li>
                         </c:if>
                    </security:protected-element>
                    <security:protected-element name="list-users">
                         <c:if test="${actionBean.navSection == 'users'}">
                             <li class="current"><d:link href="/web/users"><fmt:message key="usersmenulabel"/></d:link></li>
                         </c:if>
                         <c:if test="${actionBean.navSection != 'users'}">
                            <li><d:link href="/web/users"><fmt:message key="usersmenulabel"/></d:link></li>
                         </c:if>
                    </security:protected-element>

                    <li><d:link href="/web/logout"><fmt:message key="logoutmenulabel"/></d:link></li>
                </c:if>
                <c:if test="${actionBean.isLoggedIn == false}">
                     <c:if test="${actionBean.navSection == 'login'}">
                         <li class='current'><d:link href="/web/login"><fmt:message key="loginmenulabel"/></d:link></li>
                     </c:if>
                     <c:if test="${actionBean.navSection != 'login'}">
                             <li><d:link href="/web/login"><fmt:message key="loginmenulabel"/></d:link></li>
                     </c:if>
                </c:if>
            </ul>
        </nav>
    </header>
</div>
