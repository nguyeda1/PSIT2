package ch.avocado.share.model.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by coffeemakr on 01.04.16.
 */
public class GroupTest {

    private Group group;
    private static final String DEFAULT_NAME = "name";
    @Before
    public void setUp() {
        group = new Group("id", new ArrayList<Category>(), new Date(), new Rating(), "owner", "description", DEFAULT_NAME);
    }

    @Test
    public void testGetName() throws Exception {
        String name = "New Name";
        assertEquals(group.getName(), DEFAULT_NAME);
        assertNotEquals(DEFAULT_NAME, name);
        group.setName(name);
        assertEquals(group.getName(), name);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithNull() {
        group.setName(null);
    }

    @Test
    public void testGetReadableName() {
        assertEquals(group.getName(), group.getReadableName());
    }
}