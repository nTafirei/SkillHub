<%@ include file="/WEB-INF/tags/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/user/layout.jsp" title="Message Sent">

    <s:layout-component name="head">
    </s:layout-component>

    <s:layout-component name="contents">
        <!-- Main -->
        <div class="container">
            <div class="row aln-center">
                <div class="col-4 col-6-medium col-12-large">
                    <!-- Feature -->
                    <section>
                              <strong><fmt:message key="sentkey"/></strong>
                    </section>
                </div>
             </div>
        </div>
    </s:layout-component>
</s:layout-render>
