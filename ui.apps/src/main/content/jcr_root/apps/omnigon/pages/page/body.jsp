<%@ taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/apps/omnigon/global.jspx" %>

<%-- declares bean name and allows IDE to auto-completion for properties --%>
<jsp:useBean id="pageModel" type="com.omnigon.aem.handlebars.models.PageModel" scope="request" />
<c:set var="page" value="${pageModel}" scope="request" /> <%-- trick to use 'page' named bean --%>

<body ontouchstart="">
    <div class="main-wrapper">
        <main class="main-content">
            <cq:include script="header.jsp"/>
            <cq:include script="content.jsp"/>
            <cq:include script="footer.jsp"/>
        </main>
    </div>
</body>