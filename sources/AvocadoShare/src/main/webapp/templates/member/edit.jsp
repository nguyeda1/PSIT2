<%@ page import="ch.avocado.share.common.Encoder" %>
<%@ page import="ch.avocado.share.model.data.AccessIdentity" %>
<%@ page import="ch.avocado.share.model.data.AccessLevelEnum" %>
<%@ page import="ch.avocado.share.model.data.Members" %>
<%@ page import="ch.avocado.share.servlet.MemberServlet" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:useBean id="editMemberBean" class="ch.avocado.share.controller.EditMemberBean" />
<%
    String owner = request.getParameter(owner_parameter);
    AccessIdentity identityFromParameter = null;
    AccessLevelEnum currentLevel = AccessLevelEnum.NONE;
    if (owner != null) {
        for (Map.Entry<AccessIdentity, AccessLevelEnum> entry : members.getIdentitiesWithAccess().entrySet()) {
            if (entry.getKey().getId().equals(owner)) {
                identityFromParameter = entry.getKey();
                currentLevel = entry.getValue();
            }
        }
    }
%>
<div class="list-group-item list-group-header">
    <h2>Rechte bearbeiten</h2>
</div>
<div class="list-group-item">
    <form method="post" action="<%=request.getServletContext().getContextPath() %>members">
        <div class="form-group">
            <% if (identityFromParameter != null) { %>
                <input class="form-control" type="text" value="<%=identityFromParameter.getReadableName()%>"/>
                <input type="hidden" name="<%=MemberServlet.OWNER_ID%>"
                       value="<%=Encoder.forHtmlAttribute(identityFromParameter.getId())%>"/>
            <% } else { %>
                <label for="member-select">Mitglied auswählen</label>
                <select id="member-select" class="form-control" name="<%=MemberServlet.OWNER_ID%>">
                    <% for (AccessIdentity member : members.getUsers()) { %>
                    <option data-identity-type="user"
                            value="<%=Encoder.forHtmlAttribute(member.getId())%>"><%=member.getReadableName()%> (Benutzer)
                    </option>
                    <% } %>
                    <% for (AccessIdentity member : members.getGroups()) { %>
                    <option data-identity-type="group"
                            value="<%=Encoder.forHtmlAttribute(member.getId())%>"><%=member.getReadableName()%> (Gruppe)
                    </option>
                    <% } %>
                </select>
            <% } %>
        </div>
        <input type="hidden" name="targetId" value="<%=members.getTarget().getId()%>"/>
        <div class="form-group">
            <label for="select-level">Zugriffsrecht</label>
            <select id="select-level" name="<%=MemberServlet.ACCESS_LEVEL%>" class="form-control">
                <%=editMemberBean.getLevelOptions(currentLevel)%>
            </select>
        </div>
        <input type="hidden" name="method" value="put"/>
        <div class="form-group">
            <input type="submit" class="btn btn-primary" value="Speichern"/>
            <a href="?id=<%=Encoder.forUrlAttribute(members.getTarget().getId())%>"
               class="btn btn-secondary">Abbrechen</a>
        </div>
    </form>
</div>