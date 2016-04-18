package ch.avocado.share.model.data;

import ch.avocado.share.common.constants.sql.RatingConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunzlio1 on 18.04.2016.
 */
public class Rating {
    private List<Long> ratingUserIds;
    private long ratedObjectId;
    private float rating;

    public Rating(long ratedObjectId){
        ratingUserIds = new ArrayList<>();
        this.ratedObjectId = ratedObjectId;
    }

    public void addRatingUserId(long userId){
        ratingUserIds.add(userId);
    }

    public boolean hasUserRated(long userId){
        return ratingUserIds.contains(userId);
    }

    public void setRatedObjectId(long objectId){
        this.ratedObjectId = objectId;
    }

    public long getRatedObjectId(){
        return ratedObjectId;
    }

    public int getNumberOfRatings(){
        return ratingUserIds.size();
    }

    public float getRating(){
        return rating / ((float)ratingUserIds.size());
    }

    public void addRating(int rating){
        if(rating < RatingConstants.MIN_RATING_VALUE || rating > RatingConstants.MAX_RATING_VALUE) {
            throw new IllegalArgumentException("Rating not between " + RatingConstants.MIN_RATING_VALUE
                    + " and " + RatingConstants.MAX_RATING_VALUE);
        }
        this.rating += rating;
    }
}