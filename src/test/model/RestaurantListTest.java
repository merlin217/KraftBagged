package model;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantListTest {
    RestaurantList list1, list2;
    Restaurant restaurantA, restaurantB, restaurantC;

    @BeforeEach
    public void setUp() {
        list1 = new RestaurantList();
        list2 = new RestaurantList("Dinner Deliveries");

        restaurantA = new Restaurant("A");
        restaurantB = new Restaurant("B");
        restaurantC = new Restaurant("C");
    }

    @Test
    public void test1() {
        assertEquals("My Restaurant List", list1.getName());
        list1.setName("Lunch At Work");
        assertEquals("Lunch At Work", list1.getName());
        assertEquals("Dinner Deliveries", list2.getName());
    }

    @Test
    public void test2() {
        // Test Uniqueness
        assertTrue(list1.add(restaurantA));
        assertTrue(list1.add(restaurantB));
        assertTrue(list1.add(restaurantC));
        assertFalse(list1.add(restaurantA));
        assertEquals(3, list1.size());

        // Test addition of Collections
        assertTrue(list2.add(restaurantB));
        assertEquals(1, list2.size());
        list2.add(list1.getRestaurants());
        assertEquals(3, list2.size());

        // Test removal
        assertTrue(list1.remove(restaurantB));
        assertFalse(list1.remove(restaurantB));
        assertFalse(list1.remove(2));
        assertFalse(list1.remove(-1));
        assertEquals(2, list1.size());
        assertEquals("C", list1.get(1).getName());
    }

    @Test
    // Test suggest method
    public void test3() {
        list1.add(Arrays.asList(restaurantA, restaurantB, restaurantC));

        // Test that lastVisited is avoided, repeat for 10 times
        for (int i = 0; i < 10; i++) {
            int lastId = list1.suggestRandom().getId();
            assertTrue(lastId != list1.suggestRandom(lastId).getId());
        }
        // Test branches
        assertTrue(null == list2.suggestRandom());
        list2.add(restaurantA);
        assertEquals("A", list2.suggestRandom().getName());
    }
}
