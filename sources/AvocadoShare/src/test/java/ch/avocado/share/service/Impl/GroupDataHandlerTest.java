package ch.avocado.share.service.Impl;

import ch.avocado.share.common.ServiceLocator;
import ch.avocado.share.model.data.*;
import ch.avocado.share.service.IGroupDataHandler;
import ch.avocado.share.service.IUserDataHandler;
import ch.avocado.share.service.Mock.DatabaseConnectionHandlerMock;
import ch.avocado.share.service.Mock.ServiceLocatorModifier;
import ch.avocado.share.service.exceptions.DataHandlerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by coffeemakr on 16.04.16.
 */
public class GroupDataHandlerTest {

    private IGroupDataHandler groupDataHandler;
    private User user;
    private User userTwo;

    private String UNEXISTING_GROUP_NAME_1 = "Unexisting Group name";
    private String UNEXISTING_GROUP_NAME_2 = "Unexisting Group name 2";


    private void deleteExistingTestGroups() throws DataHandlerException {
        Group group;
        group = groupDataHandler.getGroupByName(UNEXISTING_GROUP_NAME_1);
        if(group != null) {
            System.out.println("Found group with name: " + UNEXISTING_GROUP_NAME_1);
            assertTrue(groupDataHandler.deleteGroup(group));
        }
        group = groupDataHandler.getGroupByName(UNEXISTING_GROUP_NAME_2);
        if(group != null) {
            System.out.println("Found group with name: " + UNEXISTING_GROUP_NAME_2);
            assertTrue(groupDataHandler.deleteGroup(group));
        }

    }

    @Before
    public void setUp() throws Exception {
        DatabaseConnectionHandlerMock.use();
        groupDataHandler = new GroupDataHandler();

        IUserDataHandler userDataHandler = ServiceLocator.getService(IUserDataHandler.class);

        user = new User(UserPassword.EMPTY_PASSWORD, "Prename", "Surname", "1234.jpg", new EmailAddress(false, "unexisting_email@zhaw.ch", new EmailAddressVerification(new Date(0))));
        userTwo = new User(UserPassword.EMPTY_PASSWORD, "Prename", "Surname", "1234.jpg", new EmailAddress(false, "unexisting_email2@zhaw.ch", new EmailAddressVerification(new Date(0))));

        assertNotNull(userDataHandler.addUser(user));
        assertNotNull(userDataHandler.addUser(userTwo));
        deleteExistingTestGroups();
    }


    @After
    public void tearDown() throws Exception {
        try {
            IUserDataHandler userDataHandler = ServiceLocator.getService(IUserDataHandler.class);
            userDataHandler.deleteUser(user);
            userDataHandler.deleteUser(userTwo);
            deleteExistingTestGroups();
        } finally {
            ServiceLocatorModifier.restore();
        }
    }

    @Test
    public void testGetGroups() throws Exception {
        List<String> ids = new ArrayList<>();
        Group groupOne, groupTwo;
        String id;
        // Add first group
        groupOne = new Group(user.getId(), "jhihji", UNEXISTING_GROUP_NAME_1);
        id = groupDataHandler.addGroup(groupOne);
        assertNotNull(id);
        ids.add(id);
        // Add second group
        groupTwo = new Group(user.getId(), "uiodfih", UNEXISTING_GROUP_NAME_2);
        id = groupDataHandler.addGroup(groupTwo);
        assertNotNull(id);
        ids.add(id);
        // Add another id to the list
        ids.add(user.getId());
        ids.add(null);
        List<Group> groups = groupDataHandler.getGroups(ids);
        assertFalse("groups contain null", groups.contains(null));
        assertEquals("fetched groups differ from possible groups", 2, groups.size());
        for(Group group: groups) {
            assertTrue(group.getId().equals(groupOne.getId()) || group.getId().equals(groupTwo.getId()));
        }
    }

    @Test
    public void testAddGroup() throws Exception {
        String name = UNEXISTING_GROUP_NAME_1;
        String description = "A description with \n new line";
        Group group = new Group(user.getId(), description, name);
        Date created = new Date(System.currentTimeMillis());
        String id = groupDataHandler.addGroup(group);
        assertNotNull(id);
        assertNotNull(group.getId());
        assertEquals(id, group.getId());
        Group queryGroup = groupDataHandler.getGroup(id);
        assertNotNull(queryGroup);
        assertEquals(id, queryGroup.getId());
        assertEquals(name, queryGroup.getName());
        assertEquals(description, queryGroup.getDescription());
        assertEquals(user.getId(), queryGroup.getOwnerId());
        assertTrue(created.getTime() - queryGroup.getCreationDate().getTime() < 1000);
        assertTrue(groupDataHandler.deleteGroup(group));
    }

    @Test
    public void testUpdateGroup() throws Exception {
        String name = UNEXISTING_GROUP_NAME_1;

        String description = "A description with \n new line";
        Group group = new Group(user.getId(), description, name);
        String id = groupDataHandler.addGroup(group);
        assertNotNull(id);
        assertNotNull(group.getId());

        assertNotNull(groupDataHandler.getGroup(group.getId()));

        String newName = UNEXISTING_GROUP_NAME_2;

        String newDescription = description + " new";
        group.setName(newName);
        group.setDescription(newDescription);
        group.setOwnerId(userTwo.getId());

        assertTrue(groupDataHandler.updateGroup(group));

        Group queriedGroup = groupDataHandler.getGroup(group.getId());
        assertEquals(newDescription, queriedGroup.getDescription());
        assertEquals(newName, queriedGroup.getName());
        assertEquals(group.getId(), queriedGroup.getId());
        assertEquals(userTwo.getId(), queriedGroup.getOwnerId());

        assertTrue(groupDataHandler.deleteGroup(queriedGroup));

    }

    @Test
    public void testDeleteGroup() throws Exception {
        // add group
        Group group = new Group(user.getId(), "hids", UNEXISTING_GROUP_NAME_1);
        assertNotNull(groupDataHandler.addGroup(group));
        // check if group if added
        assertNotNull(groupDataHandler.getGroup(group.getId()));
        assertNotNull(groupDataHandler.getGroupByName(group.getName()));

        // delete group
        assertTrue(groupDataHandler.deleteGroup(group));
        // check if group is deleted
        assertNull(groupDataHandler.getGroup(group.getId()));
        assertNull(groupDataHandler.getGroupByName(group.getName()));
    }

    @Test
    public void testGetGroupByName() throws Exception {
        // add group
        Group group = new Group(user.getId(), "hids", UNEXISTING_GROUP_NAME_1);
        assertNotNull(groupDataHandler.addGroup(group));

        // get group by name
        Group fetchedGroup = groupDataHandler.getGroupByName(group.getName());
        assertNotNull(fetchedGroup);

        // compare group
        assertEquals(group.getId(), fetchedGroup.getId());
        assertEquals(group.getName(), fetchedGroup.getName());
        assertEquals(group.getDescription(), fetchedGroup.getDescription());
        assertEquals(group.getOwnerId(), fetchedGroup.getOwnerId());
        assertEquals(group.getCategories(), fetchedGroup.getCategories());

        // delete group
        assertTrue(groupDataHandler.deleteGroup(group));
    }
}