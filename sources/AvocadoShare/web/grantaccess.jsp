<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:useBean id="accessBean" class="ch.avocado.share.controller.FileAccessBean" />
<jsp:setProperty name="accessBean" property="fileId" />
<jsp:setProperty name="accessBean" property="ruserId" />
<jsp:setProperty name="accessBean" property="ouserId" />
<%@include file="includes/header.jsp"%>
<% if(accessBean.grantAccess()) {
// TODO @bergmsas check access of user.
%>
<div class="alert alert-success">
    Der Benutzer wurde berechtigt.
</div>
<% } else { %>
<div class="alert alert-danger">
    Der Benutzer konnte leider nicht berechtigt werden.
</div>
<% } %>
<%@include file="includes/footer.jsp"%>