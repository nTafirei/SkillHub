<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<%@taglib prefix="security" uri="http://www.providencebehavior.com/security.tld" %>

<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Error">

    <s:layout-component name="head">
    </s:layout-component>
    <s:layout-component name="contents">

        <section class="wrapper style1">
            <div class="container">

                <div class="row">
                    <section class="6u 12u(narrower)">
                        <div class="box post">
                            <div class="inner">
                                <s:errors/>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </section>
    </s:layout-component>
</s:layout-render>
