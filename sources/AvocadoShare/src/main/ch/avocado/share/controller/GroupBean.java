package ch.avocado.share.controller;

import ch.avocado.share.common.ServiceLocator;
import ch.avocado.share.model.data.Group;
import ch.avocado.share.model.data.User;
import ch.avocado.share.model.exceptions.HttpBeanException;
import ch.avocado.share.model.exceptions.ServiceNotFoundException;
import ch.avocado.share.service.IGroupDataHandler;
import ch.avocado.share.service.IUserDataHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * GroupBean is used to create, change and delete {@link Group}s.
 */
public class GroupBean extends ResourceBean<Group> {

    private static final String ERROR_NO_NAME = "Bitte einen Namen eingeben.";
    private static final String ERROR_NO_DESCRIPTION = "Bitte eine Beschreibung angeben.";
    private static final String ERROR_INTERNAL_SERVER = "Interner Server-Fehler.";
    private static final String ERROR_GROUP_NAME_ALREADY_EXISTS = "Eine Gruppe mit diesem Namen existiert bereits.";
    public static final String ERROR_NO_SUCH_GROUP = "Gruppe existiert nicht.";
    public static final String ERROR_DATABASE = "Gruppe konnte nicht in der Datenbank gespeichert werden.";

    private String name;
    private String description;


    private boolean checkParameterName() throws HttpBeanException {
        if (name == null || name.trim().isEmpty()) {
            addFormError("name", ERROR_NO_NAME);
            return false;
        }
        name = name.trim();
        IGroupDataHandler groupDataHandler = getGroupDataHandler();
        if (groupDataHandler.getGroupByName(name) != null) {
            addFormError("name", ERROR_GROUP_NAME_ALREADY_EXISTS);
            return false;
        }
        return true;
    }

    private boolean checkParameterDescription() {
        if (description == null || description.trim().isEmpty()) {
            addFormError("description", ERROR_NO_DESCRIPTION);
            return false;
        }
        description = description.trim();
        return false;
    }

    @Override
    protected boolean hasIdentifier() {
        return name != null;
    }


    @Override
    public Group create() throws HttpBeanException {
        IGroupDataHandler groupDataHandler = getGroupDataHandler();
        IUserDataHandler userDataHandler = getUserDataHandler();
        checkParameterName();
        checkParameterDescription();
        if (!hasErrors()) {
            Group group = new Group(null, null, new Date(System.currentTimeMillis()), null, getAccessingUser().getId(), description, name, new ArrayList<User>());
            if (!groupDataHandler.addGroup(group) ||
                    !userDataHandler.addUserToGroup(getAccessingUser(), group)) {
                throw new HttpBeanException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_DATABASE);
            }
            return group;
        }
        return null;
    }

    @Override
    public Group get() throws HttpBeanException {
        IGroupDataHandler groupDataHandler = getGroupDataHandler();
        Group group = null;
        if (name != null) {
            group = groupDataHandler.getGroupByName(name);
        }
        if (group == null) {
            throw new HttpBeanException(HttpServletResponse.SC_NOT_FOUND, ERROR_NO_SUCH_GROUP);
        }
        return group;
    }

    @Override
    public Group[] index() throws HttpBeanException {
        IGroupDataHandler groupDataHandler = getGroupDataHandler();
        return groupDataHandler.getGroupsOfUser(getAccessingUser());
    }

    @Override
    public void update() throws HttpBeanException {
        IGroupDataHandler groupDataHandler = getGroupDataHandler();
        checkParameterDescription();
        checkParameterName();
        if (!hasErrors()) {
            getObject().setName(name);
            getObject().setDescription(description);
            if (!groupDataHandler.updateGroup(getObject())) {
                throw new HttpBeanException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_DATABASE);
            }
        }
    }

    @Override
    public void destroy() throws HttpBeanException {
        IGroupDataHandler groupDataHandler = getGroupDataHandler();
        if (!groupDataHandler.deleteGroup(getObject())) {
            throw new HttpBeanException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_DATABASE);
        }
    }

    @Override
    public String getAttributeName() {
        return "Group";
    }

    /**
     * Get group description
     * @return The description of the group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the group description
     * @param description The description of the group
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the group name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The group name
     */
    public String getName() {
        return name;
    }

    private IUserDataHandler getUserDataHandler() throws HttpBeanException {
        IUserDataHandler userDataHandler = null;
        try {
            userDataHandler = ServiceLocator.getService(IUserDataHandler.class);
        } catch (ServiceNotFoundException e) {
            throw new HttpBeanException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_INTERNAL_SERVER);
        }
        return userDataHandler;
    }

    private IGroupDataHandler getGroupDataHandler() throws HttpBeanException {
        IGroupDataHandler groupDataHandler = null;
        try {
            groupDataHandler = ServiceLocator.getService(IGroupDataHandler.class);
        } catch (ServiceNotFoundException e) {
            throw new HttpBeanException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_INTERNAL_SERVER);
        }
        return groupDataHandler;
    }

}
