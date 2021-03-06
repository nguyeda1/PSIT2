package ch.avocado.share.service.Mock;

import ch.avocado.share.common.ServiceLocator;
import ch.avocado.share.model.data.*;
import ch.avocado.share.service.IUserDataHandler;
import ch.avocado.share.service.exceptions.DataHandlerException;
import ch.avocado.share.service.exceptions.ObjectNotFoundException;

import java.util.*;

/**
 * Mock handler for user data
 */
public class UserDataHandlerMock extends DataHandlerMockBase<User> implements IUserDataHandler {

    public static final String EXISTING_USER0 = "1000001";
    public static final String EXISTING_USER1 = "1000002";
    public static final String EXISTING_USER2 = "1000003";
    public static final String EXISTING_USER3 = "1000004";

    /**
     * Save some precious time while testing :)
     */
    private static final UserPassword DEFAULT_PASSWORD = UserPassword.fromPassword("1234");

    public UserDataHandlerMock() {
        super();
        reset();
    }

    private void reset() {
        objects.clear();
        for (int i = 0; i < 100; i++) {
            String id = String.format("1%06d", i);
            objects.put(id, new User(id, new ArrayList<Category>(), new Date(1000), new Rating(), "description" + i, DEFAULT_PASSWORD, "prename" + i, "surname" + i, "avator" + i, new EmailAddress(true, "email" + i + "@zhaw.ch", null)));
        }
    }

    @Override
    public String addUser(User user) {
        return add(user);
    }

    @Override
    public void deleteUser(User user) throws ObjectNotFoundException {
        delete(user);
    }

    @Override
    public User getUser(String userId) throws ObjectNotFoundException {
        return get(userId);
    }

    @Override
    public User getUserByEmailAddress(String emailAddress) {
        for (User user : objects.values()) {
            if (user.getMail().getAddress().equals(emailAddress)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean addMail(User user) {
        return false;
    }

    @Override
    public void updateUser(User user) throws ObjectNotFoundException {
        update(user);
    }


    @Override
    public List<User> getUsers(Collection<String> ids) throws DataHandlerException {
        List<User> users = new ArrayList<>(ids.size());
        for(String id: ids) {
            User user = null;
            try {
                user = getUser(id);
            } catch (ObjectNotFoundException e) {
                continue;
            }
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> search(Set<String> terms) throws DataHandlerException {
        return null;
    }

    public static void use() throws Exception {
        if (!ServiceLocator.getService(IUserDataHandler.class).getClass().equals(UserDataHandlerMock.class)) {
            ServiceLocatorModifier.setService(IUserDataHandler.class, new UserDataHandlerMock());
        }
    }

    public User[] getAllUsers() {
        return getAll(User.class);
    }
}
