package ch.avocado.share.model.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static ch.avocado.share.test.Asserts.assertCategoriesEquals;
import static org.junit.Assert.*;

/**
 * Tests for {@link CategoryList}.
 */
public class CategoryListTest {

    private CategoryList categoryList;
    private ArrayList<Category> originalCategories;

    @Before
    public void setUp() throws Exception {
        originalCategories = new ArrayList<>();
        originalCategories.add(new Category("one"));
        originalCategories.add(new Category("two"));
        originalCategories.add(new Category("three"));
        categoryList = new CategoryList(originalCategories);
    }

    @Test
    public void testGetCategories() throws Exception {
        assertCategoriesEquals(originalCategories, categoryList.getCategories());
        assertCategoriesEquals(originalCategories, categoryList);
    }

    @Test
    public void testAddCategory() throws Exception {
        categoryList.add(new Category("four"));
        assertTrue(categoryList.contains(new Category("four")));
        originalCategories.add(new Category("four"));
        assertCategoriesEquals(originalCategories, categoryList);
    }

    @Test
    public void testSetCategoriesWithSame() throws Exception {
        ArrayList<Category> newCategories = new ArrayList<>();
        // first test we use an identical list
        newCategories.add(new Category("one"));
        newCategories.add(new Category("two"));
        newCategories.add(new Category("three"));
        categoryList.setCategories(newCategories);

        assertEquals(0, categoryList.getRemovedCategories().size());
        assertEquals(0, categoryList.getNewCategories().size());
        assertCategoriesEquals(newCategories, categoryList);
    }

    @Test(expected = NullPointerException.class)
    public void testSetCategoriesWithNull() throws Exception {
        categoryList.setCategories(null);
    }

    @Test
    public void testSetCategories() throws Exception {
        ArrayList<Category> newCategories = new ArrayList<>();
        // first test we use an identical list
        newCategories.add(new Category("one"));
        newCategories.add(new Category("two"));
        newCategories.add(new Category("four"));
        newCategories.add(new Category("five"));
        categoryList.setCategories(newCategories);
        assertCategoriesEquals(newCategories, categoryList);
        assertTrue(categoryList.getNewCategories().contains(new Category("four")));
        assertTrue(categoryList.getNewCategories().contains(new Category("five")));
        assertEquals(2, categoryList.getNewCategories().size());

        assertTrue(categoryList.getRemovedCategories().contains(new Category("three")));
        assertEquals(1, categoryList.getRemovedCategories().size());
    }

    @Test
    public void testRemoveCategory() throws Exception {
        assertTrue(categoryList.contains(new Category("three")));
        categoryList.remove(new Category("three"));
        assertFalse(categoryList.contains(new Category("three")));
    }

    @Test
    public void testGetNewCategories() throws Exception {
        categoryList.add(new Category("four"));
        assertTrue(categoryList.contains(new Category("four")));
        assertEquals(1, categoryList.getNewCategories().size());
        assertTrue(categoryList.getNewCategories().contains(new Category("four")));
        assertTrue(categoryList.getRemovedCategories().isEmpty());

        // Only added once
        categoryList.add(new Category("four"));
        assertEquals(1, categoryList.getNewCategories().size());

        // Not new if already exist
        categoryList.add(new Category("three"));
        assertEquals(1, categoryList.getNewCategories().size());
        assertFalse(categoryList.getNewCategories().contains(new Category("three")));
    }

    @Test
    public void testGetRemovedCategories() throws Exception {
        categoryList.remove(new Category("three"));
        assertTrue(categoryList.getRemovedCategories().contains(new Category("three")));
        assertEquals(1, categoryList.getRemovedCategories().size());
        categoryList.remove(new Category("unexisting"));
        assertFalse(categoryList.getRemovedCategories().contains(new Category("unexisting")));
        assertFalse(categoryList.contains(new Category("unexisting")));
    }
}