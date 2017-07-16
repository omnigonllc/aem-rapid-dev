<%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/apps/omnigon/global.jspx" %>
<%@ page import="com.omnigon.aem.handlebars.models.PageModel" %>
<%-- NB: 'page' will be a predefined local variable in compiled JSP, we cannot name it 'page' --%>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="com.omnigon.aem.handlebars.models.PageModel" var="pageModel" />
<%-- Lets make possible using page variable in request scope, so we need adaptTo only once per page load --%>
<c:set var="page" value="${pageModel}" scope="request" /> <%-- makes possible to be used in Handlebars as well --%>
<c:set var="pageModel" value="${pageModel}" scope="request" /> <%-- to be used in JSPs, cannot be named 'page' as jsp:useBean fails JSP to compile --%>