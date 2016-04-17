package ch.avocado.share.test;

import ch.avocado.share.model.data.User;
import org.mockito.ArgumentMatcher;

/**
 * Created by coffeemakr on 17.04.16.
 */
public class UserArgumentMatcher implements ArgumentMatcher<User> {

    private User user;

    public UserArgumentMatcher(User user) {
        this.user = user;
    }

    @Override
    public boolean matches(Object o) {
        if(!(o instanceof User)) {
            return false;
        }
        User otherUser = (User) o;
        return otherUser.getId().equals(user.getId());
    }
}
