package ch.avocado.share.service;

import ch.avocado.share.model.data.Group;
import ch.avocado.share.model.data.User;

/**
 * Created by bergm on 15/03/2016.
 */
public interface IUserDataHandler {

    /**
     * adds the given user to the Database
     * @param user user to be added
     * @return true if it could be added
     */
    boolean addUser(User user);

    /**
     * deletes the given user from the database
     * @param user user to be deleted
     * @return true if was successfully deleted
     */
    boolean deleteUser(User user);

    /**
     * Returns the user from the database
     * @param userId id of the requested user
     * @return user from database
     */
    User getUser(String userId);

    /**
     * updates a user on the database
     * @param user user with updated data
     * @return true if it was successfully updated
     */
    boolean updateUser(User user);

    /**
     * verifies the user if the code is valid and not expired
     * @param user user to be verified
     * @param code code inserted by the user
     * @return true if verification was successful
     */
    boolean verifyUser(User user, String code);

    /**
     * Adds a user to a given group
     * @param user user to be added
     * @param group group to be added to
     * @return true if user was successfully added
     */
    boolean addUserToGroup(User user, Group group);
}