package ch.avocado.share.model.data;

import java.util.Date;
import java.util.List;

/**
 * Abstract class for all AccessControlObjectBase objects which can access another object
 * @todo Rename appropriate
 */
public abstract class AccessIdentity extends AccessControlObjectBase{
    public AccessIdentity(String id, List<Category> categories, Date creationDate, float rating, String ownerId, String description) {
        super(id, categories, creationDate, rating, ownerId, description);
    }
}