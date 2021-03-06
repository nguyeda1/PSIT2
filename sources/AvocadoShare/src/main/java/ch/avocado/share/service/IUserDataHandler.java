package ch.avocado.share.service;

import ch.avocado.share.model.data.User;
import ch.avocado.share.service.exceptions.DataHandlerException;
import ch.avocado.share.service.exceptions.ObjectNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by bergm on 15/03/2016.
 */
public interface IUserDataHandler {

    /**
     * adds the given user to the Database
     * @param user user to be added
     * @return Id of the added user. Null if error occured
     * @throws DataHandlerException if an unexpected error occurs.
     */
    String addUser(User user) throws DataHandlerException;

    /**
     * deletes the given user from the database
     * @param user user to be deleted
     * @return true if was found and deleted to {@code false} indicates that the user doesn't exist.
     * @throws DataHandlerException if an unexpected error occurs.
     */
    void deleteUser(User user) throws DataHandlerException, ObjectNotFoundException;

    /**
     * Returns the user from the database
     * @param userId id of the requested user
     * @return user from database or null if the user with the given id doesn't exist.
     * @throws DataHandlerException if an unexpected error occurs.
     */
    User getUser(String userId) throws DataHandlerException, ObjectNotFoundException;

    /**
     * Returns the user from the database selected by its
     * email address.
     * @param emailAddress email address of the user
     * @return user if there is a user with this email address
     *              or otherwise null.
     */
    User getUserByEmailAddress(String emailAddress) throws DataHandlerException, ObjectNotFoundException;

    /**
     * Adds a new mail address to the database
     * @param user user with the new email
     * @return true if adding succeeded
     * @throws DataHandlerException if an unexpected error occurs.
     */
    boolean addMail(User user) throws DataHandlerException;

    /**
     * updates a user on the database
     * @param user user with updated data
     * @return true if it was successfully updated
     */
    void updateUser(User user) throws DataHandlerException, ObjectNotFoundException;

    /**
     * loads multiple users
     * @param userIdCollection list or collection of ids of the users that should be loaded from the database
     * @return List of users
     * @throws DataHandlerException
     */
    List<User> getUsers(Collection<String> userIdCollection) throws DataHandlerException;


    List<User> search(Set<String> terms) throws DataHandlerException;
}
