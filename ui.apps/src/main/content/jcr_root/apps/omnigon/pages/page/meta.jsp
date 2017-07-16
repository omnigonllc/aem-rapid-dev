<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling" %>
<%@include file="/apps/omnigon/global.jspx" %>

<%-- TODO: xss filtering of attributes and content --%>

<%-- declares bean name and allows IDE to auto-completion for properties --%>
<jsp:useBean id="pageModel" type="com.omnigon.aem.handlebars.models.PageModel" scope="request" />
<c:set var="page" value="${pageModel}" scope="request" /> <%-- trick to use 'page' named bean --%>

<title>${page.title}</title>

<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>

