package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class RestaurantTest {
    Restaurant restaurant1, restaurant2, restaurant3;

    @BeforeEach
    public void setUp() {
        restaurant1 = new Restaurant("Frank's");
        restaurant2 = new Restaurant("John's", 8);
        restaurant3 = new Restaurant("Jack's", 12);
    }

    @Test
    public void test1() {
        assertEquals(1, restaurant1.getId());
        assertEquals(2, restaurant2.getId());
        assertEquals(3, restaurant3.getId());
        assertEquals("Frank's", restaurant1.getName());
        assertEquals("John's", restaurant2.getName());
        assertEquals("Jack's", restaurant3.getName());

        String expected = "Jack's | Avg Rating: 0 | ID: 3";
        assertEquals(expected, restaurant3.toString());
    }

    @Test
    public void test2() {
        assertEquals(0, restaurant1.getRating());
        assertEquals(8, restaurant2.getRating());
        assertEquals(0, restaurant3.getRating());

        restaurant1.updateRating(10);
        restaurant2.updateRating(1);
        assertEquals(10, restaurant1.getRatingRounded());
        assertEquals(5, restaurant2.getRatingRounded());

        restaurant2.updateRating(4);
        assertEquals(4, restaurant2.getRatingRounded());
    }

    @Test
    public void testEquals() {
        Restaurant restaurant4 = new Restaurant("Frank's");
        Restaurant restaurant5 = restaurant1;
        
        assertTrue(restaurant1.equals(restaurant4));
        assertTrue(restaurant1.equals(restaurant5));
        assertFalse(restaurant1.equals(restaurant2));
    }


}
