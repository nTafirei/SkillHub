<%@ include file="/WEB-INF/tags/taglibs.jsp" %>

<s:layout-definition>

    <!DOCTYPE html "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head>

        <title>${title}</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <meta name="description" content="Security"/>
        <meta name="keywords" content="Security"/>
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <meta name="description" content=""/>
        <meta name="keywords" content=""/>
        <link rel="stylesheet" href="${actionBean.cssPath}/table.css"/>

        <!--[if lte IE 8]>
        <script src="${actionBean.cssPath}/ie/html5shiv.js"></script><![endif]-->
        <script src="${actionBean.scriptPath}/jquery-1.12.4.min.js"></script>
          <script src="${actionBean.scriptPath}/jquery.dragoptions.js"></script>
        <script src="${actionBean.scriptPath}/jquery.dropotron.min.js"></script>
        <script src="${actionBean.scriptPath}/skel.min.js"></script>
        <script src="${actionBean.scriptPath}/skel-layers.min.js"></script>
        <script src="${actionBean.scriptPath}/init.js"></script>
        <noscript>
            <link rel="stylesheet" href="${actionBean.cssPath}/skel.css"/>
            <link rel="stylesheet" href="${actionBean.cssPath}/style.css"/>
            <link rel="stylesheet" href="${actionBean.cssPath}/style-desktop.css"/>
        </noscript>
        <!--[if lte IE 8]>
        <link rel="stylesheet" href="${actionBean.cssPath}/ie/v8.css"/><![endif]-->
        <s:layout-component name="head">
        </s:layout-component>
    </head>

    <body class="homepage">

    <s:layout-component name="header">
        <jsp:include page="header.jsp"/>
    </s:layout-component>

    <s:layout-component name="contents">
    </s:layout-component>
    </body>
    </html>
</s:layout-definition>
