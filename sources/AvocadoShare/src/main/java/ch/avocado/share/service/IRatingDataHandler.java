package ch.avocado.share.service;

import ch.avocado.share.model.data.Rating;
import ch.avocado.share.service.exceptions.DataHandlerException;

/**
 * Created by User on 18.04.2016.
 */
public interface IRatingDataHandler {
    /**
     * Gets the Rating object for an AccessControlObject.
     * @param ratedObjectId The id of the AccessControlObject, for which the Ranking should be returned.
     * @return The Ranking for the AccessControlObject.
     * @throws DataHandlerException This Exception is thrown, if there is an error while accessing/reading or writing in the db.
     */
    Rating getRatingForObject(int ratedObjectId) throws DataHandlerException;

    /**
     * Gets the rating as integer, which the a User gave to an AccessControlObject.
     * @param ratingUserId The id of the User, which had rated.
     * @param ratedObjectId The id of the AccessControlObject, which was rated
     * @return The rating as integer, which the a User gave to the AccessControlObject.
     * @throws DataHandlerException This Exception is thrown, if there is an error while accessing/reading or writing in the db.
     */
    int getRatingForUserAndObject(long ratingUserId, long ratedObjectId) throws DataHandlerException;

    /**
     * Insert a new Ranking int to the database.
     * @param ratedAccessObjectId The id of the rated AccessControlObject.
     * @param userId The id of the rating User.
     * @param rating The rating, which the User gave to the AccessControlObject.
     * @return The primary key, from the inserted dataset.
     * @throws DataHandlerException This Exception is thrown, if there is an error while accessing/reading or writing in the db.
     */
    long addRating(long ratedAccessObjectId, long userId, int rating) throws DataHandlerException;

    /**
     * Deletes a Ranking from the database.
     * @param ratedAccessObjectId The id of the rated AccessControlObject.
     * @param userId The id of the rating User.
     * @return True if the Ranking was deleted from the database. False if not.
     * @throws DataHandlerException This Exception is thrown, if there is an error while accessing/reading or writing in the db.
     */
    boolean deleteRating(long ratedAccessObjectId, long userId) throws DataHandlerException;

    /**
     * Updated a Ranking in the database.
     * @param ratedAccessObjectId The id of the rated AccessControlObject.
     * @param userId The id of the rating User.
     * @param rating The changed rating, which the User gave to the AccessControlObject.
     * @return True if the Ranking was updated correctly in the database. False if not.
     * @throws DataHandlerException This Exception is thrown, if there is an error while accessing/reading or writing in the db.
     */
    boolean updateRating(long ratedAccessObjectId, long userId, int rating) throws DataHandlerException;
}